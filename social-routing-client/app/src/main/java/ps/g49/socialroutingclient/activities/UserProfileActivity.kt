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
import ps.g49.socialroutingclient.model.domainModel.UserAccount
import ps.g49.socialroutingclient.model.inputModel.socialRouting.PersonInput
import ps.g49.socialroutingclient.model.inputModel.socialRouting.RouteInput
import ps.g49.socialroutingclient.model.inputModel.socialRouting.SimplifiedRouteInputCollection
import ps.g49.socialroutingclient.adapters.OnRouteListener
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

        // TODO("receive the correct user url")
        socialRoutingApplication = application as SocialRoutingApplication
        val personUrl = socialRoutingApplication.getUser().userUrl.split("/")
        val correctUrl = getString(R.string.api_url) + "persons/" + personUrl[5]
        getUserProfileInfo(correctUrl)
        setView(socialRoutingApplication.getUser())
    }

    private fun getUserProfileInfo(userUrl: String) {
        val liveData = viewModel.getUser(userUrl)
        handleRequestedData(liveData, ::requestSuccessHandlerUserProfile)
    }

    private fun setView(user: UserAccount) {
        toolbar_layout.title = user.name
    }

    private fun requestUserRoutes(personInput: PersonInput?) {
        val personUrl = socialRoutingApplication.getUser().userUrl.split("/")
        val correctUrl = getString(R.string.api_url) + "persons/" + personUrl[5] + "/routes"
        val liveDataRoutes = viewModel.getUserRoutesFromUrl(/*personInput!!.routesUrl*/correctUrl)
        handleRequestedData(liveDataRoutes, ::requestSuccessHandlerUserRoutes)
    }

    private fun setRecyclerView(routesList: List<RouteInput>) {
        val adapter = SearchRoutesAdapter(this, routesList, this, true)
        val layoutManager = LinearLayoutManager(applicationContext)
        userRoutesRecyclerView.layoutManager = layoutManager
        userRoutesRecyclerView.itemAnimator = DefaultItemAnimator()
        userRoutesRecyclerView.adapter = adapter
    }

    override fun onRouteClick(position: Int) {
        if (routesInputs.isNotEmpty()) {
            val routeIdIntentMessage = getString(R.string.route_id_intent_message)
            val routeIntentMessage = getString(R.string.route_intent_message)
            val routeInput = routesInputs[position]
            val intent = Intent(this, RouteRepresentationActivity::class.java)
            //intent.putExtra(routeIntentMessage, routeInput.routeUrl)
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
