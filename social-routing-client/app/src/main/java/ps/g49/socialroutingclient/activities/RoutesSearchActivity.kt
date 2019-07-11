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
import ps.g49.socialroutingclient.adapters.OnRouteListener
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
    private lateinit var routesSearched: List<RouteInput>
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
    }

    private fun requestLocationIdentifier(locationName: String) {
        val liveData = googleViewModel.getGeoCoordinatesFromLocation(locationName)
        handleRequestedData(liveData, ::requestSuccessHandlerLocationIdentifier, ::requestErrorHandlerLocationIdentifier)
    }

    private fun requestSuccessHandlerLocationIdentifier(geoCodingResponse: GeoCodingResponse?) {
        val placeId = geoCodingResponse!!.results.first {
            it.types.contains("locality") && it.types.contains("political")
        }.place_id
        searchRoutes(placeId, searchParams.categories, searchParams.locationName)
    }

    private fun requestErrorHandlerLocationIdentifier(msg: String?) {
        showToast("Could not find the specified location.")
    }

    private fun searchRoutes(locationIdentifier: String, categories: List<Category>, duration: String) {
        val searchRoutesUrl = socialRoutingApplication
            .getSocialRoutingRootResource()
            .routeSearchUrl
        val liveData = socialRoutingViewModel.searchRoutes(searchRoutesUrl, locationIdentifier, categories, duration)
        handleRequestedData(
            liveData,
            ::requestSuccessHandlerRouteSearch,
            ::requestErrorHandlerSearch
        )
    }

    private fun requestSuccessHandlerRouteSearch(simplifiedRouteInputCollection: SimplifiedRouteInputCollection?) {
        val routesSearched = simplifiedRouteInputCollection!!.routes
        if (routesSearched.isEmpty())
            emptySearchRoutesTextView.visibility = View.VISIBLE
        else
            setRecyclerView(routesSearched)
    }

    private fun requestErrorHandlerSearch(msg: String) {
        emptySearchRoutesTextView.visibility = View.VISIBLE
    }

    private fun setRecyclerView(list: List<RouteInput>) {
        val adapter = SearchRoutesAdapter(this, list, this, false)
        val layoutManager = LinearLayoutManager(applicationContext)
        routesSearched = list
        routesRecyclerView.layoutManager = layoutManager
        routesRecyclerView.itemAnimator = DefaultItemAnimator()
        routesRecyclerView.adapter = adapter
        routesRecyclerView.addOnScrollListener(ScrollListener {
            TODO ()
        })
    }

    override fun onRouteClick(position: Int) {
        if (routesSearched.isNotEmpty()) {
            val routeIntentMessage = getString(R.string.route_intent_message)
            val routeUrl = routesSearched[position].routeUrl.split("/")
            val url = "http://10.0.2.2:8080/api.sr/" + routeUrl[routeUrl.size - 2] + routeUrl[routeUrl.size - 1]

            val intent = Intent(this, RouteRepresentationActivity::class.java)
            intent.putExtra(routeIntentMessage, url)
            startActivity(intent)
        }
    }

}
