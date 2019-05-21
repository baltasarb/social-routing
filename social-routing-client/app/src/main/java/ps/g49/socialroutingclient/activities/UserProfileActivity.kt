package ps.g49.socialroutingclient.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.content_user_profile.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.inputModel.PersonInput
import ps.g49.socialroutingclient.model.inputModel.RouteInput
import ps.g49.socialroutingclient.utils.OnRouteListener
import ps.g49.socialroutingclient.utils.Resource
import ps.g49.socialroutingclient.utils.UserCreatedRoutesAdapter
import ps.g49.socialroutingclient.viewModel.UserProfileViewModel

class UserProfileActivity : BaseActivity(), OnRouteListener {

    private lateinit var viewModel: UserProfileViewModel
    private lateinit var routesInputs: List<RouteInput>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setSupportActionBar(toolbar)

        viewModel = getViewModel()

        getUserProfileInfo(200)
    }

    private fun getUserProfileInfo(userId: Int) {
        val liveData = viewModel.getUser(userId)
        handleRequestedData(liveData, ::requestSuccessHandler)
    }

    fun requestSuccessHandler(personInput: PersonInput) {
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
