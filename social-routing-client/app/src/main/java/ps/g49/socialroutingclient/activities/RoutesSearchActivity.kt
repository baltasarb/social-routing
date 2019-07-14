package ps.g49.socialroutingclient.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_search_routes.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.adapters.listeners.OnRouteListener
import ps.g49.socialroutingclient.adapters.SearchRoutesAdapter
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.model.domainModel.Category
import ps.g49.socialroutingclient.model.domainModel.Search
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.GeoCodingResponse
import ps.g49.socialroutingclient.model.inputModel.socialRouting.RouteInput
import ps.g49.socialroutingclient.model.inputModel.socialRouting.SimplifiedRouteInputCollection
import ps.g49.socialroutingclient.utils.ScrollListener
import ps.g49.socialroutingclient.viewModel.GoogleViewModel
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import javax.inject.Inject

class RoutesSearchActivity : BaseActivity(), OnRouteListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var socialRoutingViewModel: SocialRoutingViewModel
    private lateinit var googleViewModel: GoogleViewModel
    private lateinit var routesSearched: MutableList<RouteInput>
    private lateinit var socialRoutingApplication: SocialRoutingApplication
    private lateinit var searchParams: Search

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_routes)

        socialRoutingApplication = application as SocialRoutingApplication
        socialRoutingViewModel = getViewModel(viewModelFactory)
        googleViewModel = getViewModel(viewModelFactory)

        searchParams = intent.getParcelableExtra(getString(R.string.location_intent_message))
        requestLocationIdentifier(searchParams.locationName)

        routesSearched = mutableListOf()
    }

    override fun initView() {}

    private fun requestLocationIdentifier(locationName: String) {
        val liveData = googleViewModel.getGeoCoordinatesFromLocation(locationName)
        handleRequestedData(liveData, ::successHandlerLocationIdentifier, ::errorHandlerLocationIdentifier)
    }

    private fun successHandlerLocationIdentifier(geoCodingResponse: GeoCodingResponse?) {
        val placeId = geoCodingResponse!!.results.first {
            it.types.contains("locality") && it.types.contains("political")
        }.place_id
        requestSearchRoute(placeId, searchParams.categories, searchParams.duration)
    }

    private fun errorHandlerLocationIdentifier() {
        showToast("Could not find the specified location.")
        emptySearchRoutesNavigationTextView.visibility = View.GONE
    }

    private fun requestSearchRoute(locationIdentifier: String, categories: List<Category>, duration: String) {
        val searchRoutesUrl = socialRoutingApplication
            .getSocialRoutingRootResource()
            .routeSearchUrl
        val liveData = socialRoutingViewModel.searchRoutes(searchRoutesUrl, locationIdentifier, categories, duration)
        handleRequestedData(
            liveData,
            ::successHandlerRouteSearch,
            ::errorHandlerRouteSearch
        )
    }

    private fun successHandlerRouteSearch(simplifiedRouteInputCollection: SimplifiedRouteInputCollection?) {
        val routesResult = simplifiedRouteInputCollection!!.routes
        if (routesResult.isEmpty())
            emptySearchRoutesNavigationTextView.visibility = View.VISIBLE
        else {
            val list = simplifiedRouteInputCollection.routes
            routesSearched.addAll(list)
            setRecyclerView(simplifiedRouteInputCollection)
        }
    }

    private fun errorHandlerRouteSearch() {
        emptySearchRoutesNavigationTextView.visibility = View.VISIBLE
    }

    private fun setRecyclerView(simplifiedRouteInputCollection: SimplifiedRouteInputCollection) {
        val adapter = SearchRoutesAdapter(this, routesSearched, this, false)
        val layoutManager = LinearLayoutManager(applicationContext)
        routesRecyclerView.layoutManager = layoutManager
        routesRecyclerView.itemAnimator = DefaultItemAnimator()
        routesRecyclerView.adapter = adapter
        routesRecyclerView.addOnScrollListener(ScrollListener {
            if (simplifiedRouteInputCollection.next != null) {
                val liveData = socialRoutingViewModel.genericGet<SimplifiedRouteInputCollection>(simplifiedRouteInputCollection.next)
                handleRequestedData(
                    liveData,
                    ::successHandlerRouteSearch,
                    ::errorHandlerRouteSearch
                )
            }
        })
    }

    override fun onRouteClick(position: Int) {
        if (routesSearched.isNotEmpty()) {
            val routeIntentMessage = getString(R.string.route_creation_intent_message)
            val correctUrl = socialRoutingApplication.setCorrectUrlToDevice(routesSearched[position].routeUrl)

            val intent = Intent(this, RouteRepresentationActivity::class.java)
            intent.putExtra(routeIntentMessage, correctUrl)
            startActivity(intent)
        }
    }

}
