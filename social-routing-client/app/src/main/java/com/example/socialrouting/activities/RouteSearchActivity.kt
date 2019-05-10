package com.example.socialrouting.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.socialrouting.R
import com.example.socialrouting.SocialRoutingApplication
import com.example.socialrouting.kotlinx.getViewModel
import com.example.socialrouting.model.inputModel.RouteSearchInput
import com.example.socialrouting.utils.OnRouteListener
import com.example.socialrouting.utils.SearchRoutesAdapter
import com.example.socialrouting.viewModel.RouteViewModel
import kotlinx.android.synthetic.main.activity_search_routes.*

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
        handleRequestedData(liveData)
    }

    override fun <T> requestSuccessHandler(result: T) {
        val list = result as List<RouteSearchInput>
        routesSearched = list
        if (list.isEmpty())
            emptySearchRoutesTextView.visibility = View.VISIBLE
        else {
            emptySearchRoutesTextView.visibility = View.INVISIBLE
            setRecyclerView(list)
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
