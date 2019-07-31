package ps.g49.socialroutingclient.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_user_profile.*
import kotlinx.android.synthetic.main.content_user_profile.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.adapters.SearchRoutesAdapter
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.inputModel.socialRouting.PersonInput
import ps.g49.socialroutingclient.model.inputModel.socialRouting.RouteInput
import ps.g49.socialroutingclient.model.inputModel.socialRouting.SimplifiedRouteInputCollection
import ps.g49.socialroutingclient.adapters.listeners.OnRouteListener
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.viewModel.UserProfileViewModel
import javax.inject.Inject

class UserProfileActivity : BaseActivity(), OnRouteListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var viewModel: UserProfileViewModel
    private lateinit var routesInputs: List<RouteInput>
    private lateinit var socialRoutingApplication: SocialRoutingApplication

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        setSupportActionBar(toolbar)

        viewModel = getViewModel(viewModelFactory)
        socialRoutingApplication = application as SocialRoutingApplication
        initView()

        val correctUrl = socialRoutingApplication.setCorrectUrlToDevice(socialRoutingApplication.getUser().userUrl)
        requestUserProfileInfo(correctUrl)
    }

    override fun initView() {
        toolbar_layout.title = socialRoutingApplication.getUser().name
    }

    private fun requestUserProfileInfo(userUrl: String) {
        val liveData = viewModel.getUser(userUrl)
        handleRequestedData(liveData, ::successHandlerUserProfile)
    }

    private fun successHandlerUserProfile(personInput: PersonInput?) {
        userRatingBar.rating = personInput!!.rating.toFloat()
        requestUserRoutes(personInput)
    }

    private fun requestUserRoutes(personInput: PersonInput?) {
        val correctUrl = socialRoutingApplication.setCorrectUrlToDevice(personInput!!.routesUrl)
        val liveDataRoutes = viewModel.getUserRoutesFromUrl(correctUrl)
        handleRequestedData(liveDataRoutes, ::successHandlerUserRoutes)
    }

    private fun successHandlerUserRoutes(simplifiedRouteInputCollection: SimplifiedRouteInputCollection?) {
        val routesList = simplifiedRouteInputCollection!!.routes

        if (routesList.isEmpty())
            emptyUserRoutesTextView.visibility = View.VISIBLE
        else {
            emptyUserRoutesTextView.visibility = View.GONE
            setRecyclerView(simplifiedRouteInputCollection)
        }
    }

    private fun setRecyclerView(simplifiedRouteInputCollection: SimplifiedRouteInputCollection) {
        val routesList = simplifiedRouteInputCollection.routes
        val adapter = SearchRoutesAdapter(this, routesList, this, true)
        routesInputs = routesList
        val layoutManager = LinearLayoutManager(applicationContext)
        userRoutesRecyclerView.layoutManager = layoutManager
        userRoutesRecyclerView.itemAnimator = DefaultItemAnimator()
        userRoutesRecyclerView.adapter = adapter
    }

    override fun onRouteClick(position: Int) {
        if (routesInputs.isNotEmpty()) {
            val routeIntentMessage = getString(R.string.route_creation_intent_message)
            val routeInput = routesInputs[position]
            val correctUrl = socialRoutingApplication.setCorrectUrlToDevice(routeInput.routeUrl)
            val intent = Intent(this, RouteRepresentationActivity::class.java)
            intent.putExtra(routeIntentMessage, correctUrl)
            startActivity(intent)
        }
    }

}
