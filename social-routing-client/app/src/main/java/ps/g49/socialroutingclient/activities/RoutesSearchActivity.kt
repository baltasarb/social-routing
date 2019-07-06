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
import ps.g49.socialroutingclient.model.inputModel.socialRouting.RouteInput
import ps.g49.socialroutingclient.model.inputModel.socialRouting.SimplifiedRouteInputCollection
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import javax.inject.Inject

class RoutesSearchActivity : BaseActivity(), OnRouteListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var socialRoutingViewModel: SocialRoutingViewModel
    private lateinit var routesSearched: List<RouteInput>
    private lateinit var socialRoutingApplication: SocialRoutingApplication
    private lateinit var locationString: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_routes)

        socialRoutingViewModel = getViewModel(viewModelFactory)
        socialRoutingApplication = application as SocialRoutingApplication

        locationString = intent.getStringExtra(getString(R.string.location_intent_message))
        searchRoutes(locationString)
    }

    private fun searchRoutes(location: String) {
        val searchRoutesUrl = socialRoutingApplication
            .getSocialRoutingRootResource()
            .routeSearchUrl
        val liveData = socialRoutingViewModel.searchRoutes(searchRoutesUrl, location)
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
        val adapter = SearchRoutesAdapter(list, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        routesSearched = list
        routesRecyclerView.layoutManager = layoutManager
        routesRecyclerView.itemAnimator = DefaultItemAnimator()
        routesRecyclerView.adapter = adapter
    }

    override fun onRouteClick(position: Int) {
        if (routesSearched.isNotEmpty()) {
            val routeIdIntentMessage = getString(R.string.route_id_intent_message)
            val routeIntentMessage = getString(R.string.route_intent_message)
            val routeUrl = getString(R.string.route_url)

            val routeInput = routesSearched.get(position)
            val intent = Intent(this, RouteRepresentationActivity::class.java)
            intent.putExtra(routeIntentMessage, routeUrl)
            intent.putExtra(routeIdIntentMessage, routeInput.identifier)
            startActivity(intent)
        }
    }

}
