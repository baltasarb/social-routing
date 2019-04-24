package com.example.socialrouting.activities

import android.os.Bundle
import android.view.View
import com.example.socialrouting.R
import com.example.socialrouting.SocialRoutingApplication
import com.example.socialrouting.kotlinx.getViewModel
import com.example.socialrouting.model.inputModel.RouteSearchInput
import com.example.socialrouting.utils.SearchRoutesAdapter
import com.example.socialrouting.viewModel.RouteViewModel
import kotlinx.android.synthetic.main.activity_search_routes.*

class SearchRoutesActivity : BaseActivity() {

    private lateinit var routeViewModel: RouteViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_routes)

        routeViewModel = getViewModel()
        setView(100)
    }

    private fun setView(userId: Int) {
        val liveData = routeViewModel.searchRoutes()
        handleRequestedData(liveData)
    }

    override fun <T> requestSuccessHandler(result: T) {
        val list = result as List<RouteSearchInput>
        if (list.isEmpty())
            emptySearchRoutesTextView.visibility = View.VISIBLE
        else {
            emptySearchRoutesTextView.visibility = View.INVISIBLE
            routesSearchListView.adapter = SearchRoutesAdapter(getSocialRoutingApplication(), list)
            routesSearchListView.setOnItemClickListener { _, _, position, _ ->
                // TODO
            }
        }

    }

    private fun getSocialRoutingApplication() = this@SearchRoutesActivity.application as SocialRoutingApplication
}
