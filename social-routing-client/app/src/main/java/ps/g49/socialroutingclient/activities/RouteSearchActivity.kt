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
import ps.g49.socialroutingclient.model.inputModel.RouteSearchInput
import ps.g49.socialroutingclient.utils.OnRouteListener
import ps.g49.socialroutingclient.utils.SearchRoutesAdapter
import ps.g49.socialroutingclient.viewModel.RouteViewModel

class RouteSearchActivity : BaseActivity(), OnRouteListener {

    private lateinit var routeViewModel: RouteViewModel
    private lateinit var routesSearched: List<RouteSearchInput>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_routes)

        routeViewModel = getViewModel()
        searchRoutes()
    }

    private fun searchRoutes() {
        val liveData = routeViewModel.searchRoutes()
        handleRequestedData(liveData, ::requestSuccessHandlerRouteSearch)
    }

    fun requestSuccessHandlerRouteSearch(result: List<RouteSearchInput>) {
        routesSearched = result
        if (result.isEmpty())
            emptySearchRoutesTextView.visibility = View.VISIBLE
        else {
            emptySearchRoutesTextView.visibility = View.INVISIBLE
            setRecyclerView(result)
        }

    }

    private fun setRecyclerView(list: List<RouteSearchInput>) {
        val adapter = SearchRoutesAdapter(list, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        routesRecyclerView.layoutManager = layoutManager
        routesRecyclerView.itemAnimator = DefaultItemAnimator()
        routesRecyclerView.adapter = adapter
    }

    override fun onRouteClick(position: Int) {
        if (routesSearched.isNotEmpty()) {
            val routeInput = routesSearched.get(position)
            val intent = Intent(this, RouteRepresentationActivity::class.java)
            intent.putExtra(RouteRepresentationActivity.ROUTE_ID_MESSAGE, routeInput.identifier)
            startActivity(intent)
        }
    }

    private fun getSocialRoutingApplication() = this@RouteSearchActivity.application as SocialRoutingApplication
}
