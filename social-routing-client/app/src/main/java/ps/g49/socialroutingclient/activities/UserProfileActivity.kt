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
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.UserAccount
import ps.g49.socialroutingclient.model.inputModel.PersonInput
import ps.g49.socialroutingclient.model.inputModel.RouteInput
import ps.g49.socialroutingclient.model.inputModel.SimplifiedRouteInputCollection
import ps.g49.socialroutingclient.utils.OnRouteListener
import ps.g49.socialroutingclient.adapters.UserCreatedRoutesAdapter
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
        getUserProfileInfo(socialRoutingApplication.getUser().id)
        setView(socialRoutingApplication.getUser())
    }

    private fun getUserProfileInfo(userId: Int) {
        val liveData = viewModel.getUser(userId)
        handleRequestedData(liveData, ::requestSuccessHandlerUserProfile)
    }

    private fun setView(user: UserAccount) {
        toolbar_layout.title = user.name
        userEmailTextView.text = user.email
    }

    private fun requestUserRoutes(personInput: PersonInput?) {
        val liveDataRoutes = viewModel.getUserRoutesFromUrl(personInput!!.routesUrl)
        handleRequestedData(liveDataRoutes, ::requestSuccessHandlerUserRoutes)
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
            val routeIdIntentMessage = getString(R.string.route_id_intent_message)
            val routeInput = routesInputs[position]
            val intent = Intent(this, RouteRepresentationActivity::class.java)
            intent.putExtra(routeIdIntentMessage, routeInput.identifier)
            startActivity(intent)
        }
    }

    private fun requestSuccessHandlerUserProfile(personInput: PersonInput?) {
        userRatingBar.rating = personInput!!.rating.toFloat()
        requestUserRoutes(personInput)
    }

    private fun requestSuccessHandlerUserRoutes(simplifiedRouteInputCollection: SimplifiedRouteInputCollection?) {
        val routesList = simplifiedRouteInputCollection!!.routes
        routesInputs = routesList
        if (routesList.isEmpty())
            emptyUserRoutesTextView.visibility = View.VISIBLE
        else {
            emptyUserRoutesTextView.visibility = View.INVISIBLE
            setRecyclerView(routesList)
        }
    }

}
