package com.example.socialrouting.activities

import android.os.Bundle
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.socialrouting.R
import com.example.socialrouting.SocialRoutingApplication
import com.example.socialrouting.kotlinx.getViewModel
import com.example.socialrouting.model.inputModel.PersonInput
import com.example.socialrouting.model.inputModel.RouteInput
import com.example.socialrouting.utils.Resource
import com.example.socialrouting.utils.UserCreatedRoutesAdapter
import com.example.socialrouting.viewModel.UserProfileViewModel
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.content_user_profile.*

class UserProfileActivity : BaseActivity() {

    private lateinit var viewModel: UserProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setSupportActionBar(toolbar)

        viewModel = getViewModel()
        setView(100)
    }

    private fun setView(userId: Int) {
        val liveData = viewModel.getUser(userId)
        handleRequestedData(liveData)
    }

    override fun <T> requestSuccessHandler(result: T) {
        val personInput = result as PersonInput
        toolbar_layout.title = personInput.name
        userRatingBar.rating = personInput.rating.toFloat()
        userEmailTextView.text = personInput.email
        val liveDataRoutes = viewModel.getUserRoutesFromUrl(personInput.routesUrl)
        handleRequestUserRoutes(liveDataRoutes)
    }

    private fun handleRequestUserRoutes(data: LiveData<Resource<List<RouteInput>>>) {
        data.observe(this, Observer {
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
                    if (routesList.isEmpty())
                        emptyUserRoutesTextView.visibility = View.VISIBLE
                    else {
                        emptyUserRoutesTextView.visibility = View.INVISIBLE
                        userRoutesCreatedListView.adapter = UserCreatedRoutesAdapter(getSocialRoutingApplication(), routesList)
                        userRoutesCreatedListView.setOnItemClickListener{ _, _, position, _ ->
                            // TODO on click route
                        }
                    }
                }
            }
        })
    }

    private fun getSocialRoutingApplication() = this@UserProfileActivity.application as SocialRoutingApplication
}
