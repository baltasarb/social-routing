package com.example.socialrouting.activities

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.socialrouting.R
import com.example.socialrouting.SocialRoutingApplication
import com.example.socialrouting.kotlinx.getViewModel
import com.example.socialrouting.model.inputModel.PersonInput
import com.example.socialrouting.model.inputModel.RouteInput
import com.example.socialrouting.utils.OnRouteListener
import com.example.socialrouting.utils.Resource
import com.example.socialrouting.utils.UserCreatedRoutesAdapter
import com.example.socialrouting.viewModel.UserProfileViewModel
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.content_user_profile.*

class UserProfileActivity : BaseActivity(), OnRouteListener {

    private lateinit var viewModel: UserProfileViewModel
    private lateinit var routesInputs: List<RouteInput>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setSupportActionBar(toolbar)

        viewModel = getViewModel()

        getUserProfileInfo(100)
    }

    private fun getUserProfileInfo(userId: Int) {
        val liveData = viewModel.getUser(userId)
        handleRequestedData(liveData)
    }

    override fun <T> requestSuccessHandler(result: T) {
        val personInput = result as PersonInput
        setView(personInput)
        requestUserRoutes(personInput)
    }

    private fun setView(personInput: PersonInput) {
        toolbar_layout.title = personInput.name
        userRatingBar.rating = personInput.rating.toFloat()
        userEmailTextView.text = personInput.email
    }

    private fun requestUserRoutes(personInput: PersonInput) {
        val liveDataRoutes = viewModel.getUserRoutesFromUrl(personInput.routesUrl)

        liveDataRoutes.observe(this, Observer {
            when (it.status) {
                Resource.Status.LOADING -> {
                    requestLoadingHandler()
                }
                Resource.Status.ERROR -> {
                    //stopSpinner()
                    requestErrorHandler(it.message!!)
                }
                Resource.Status.SUCCESS -> {
                    //stopSpinner()
                    val routesList = it.data!!
                    routesInputs = routesList
                    if (routesList.isEmpty())
                        emptyUserRoutesTextView.visibility = View.VISIBLE
                    else {
                        emptyUserRoutesTextView.visibility = View.INVISIBLE
                        setRecyclerView(routesList)
                    }
                }
            }
        })
    }

    private fun setRecyclerView(routesList: List<RouteInput>) {
        val adapter = UserCreatedRoutesAdapter(routesList, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        userRoutesRecyclerView.layoutManager = layoutManager
        userRoutesRecyclerView.itemAnimator = DefaultItemAnimator()
        userRoutesRecyclerView.adapter = adapter
    }

    override fun onRouteClick(position: Int) {
        if (routesInputs.isNotEmpty()) {
            val routeInput = routesInputs.get(position)
            val intent = Intent(this, RouteRepresentationActivity::class.java)
            intent.putExtra(RouteRepresentationActivity.ROUTE_ID_MESSAGE, routeInput.identifier)
            startActivity(intent)
        }

    }

    private fun getSocialRoutingApplication() = this@UserProfileActivity.application as SocialRoutingApplication
}
