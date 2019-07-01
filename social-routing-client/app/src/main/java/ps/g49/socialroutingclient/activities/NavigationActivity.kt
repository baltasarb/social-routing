package ps.g49.socialroutingclient.activities

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.appcompat.app.ActionBarDrawerToggle
import android.view.MenuItem
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.widget.Toolbar
import android.view.Menu
import android.view.View
import android.view.WindowManager
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.android.synthetic.main.activity_search_routes.*
import kotlinx.android.synthetic.main.content_navigation.*
import kotlinx.android.synthetic.main.nav_header_navigation.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.adapters.SearchRoutesAdapter
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.inputModel.socialRouting.RouteInput
import ps.g49.socialroutingclient.model.inputModel.socialRouting.SimplifiedRouteInputCollection
import ps.g49.socialroutingclient.services.LocationService
import ps.g49.socialroutingclient.utils.OnRouteListener
import ps.g49.socialroutingclient.viewModel.GoogleViewModel
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import javax.inject.Inject

class NavigationActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, OnRouteListener{

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var googleViewModel: GoogleViewModel
    private lateinit var socialRoutingViewModel: SocialRoutingViewModel
    private lateinit var socialRoutingApplication: SocialRoutingApplication
    private lateinit var routesSearched: List<RouteInput>
    private lateinit var currentLocation: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_navigation)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        stopSpinner()

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener(this)
        socialRoutingApplication = application as SocialRoutingApplication
        googleViewModel = getViewModel(viewModelFactory)
        socialRoutingViewModel = getViewModel(viewModelFactory)

        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, IntentFilter(LocationService.INTENT_FILTER))
        startService(Intent(this, LocationService::class.java))
    }

    /*override fun onStart() {
        super.onStart()
        requestSuccessHandlerLocationRequest(currentLocation)
    }*/

    private val mMessageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent != null) {
                val location = intent.getParcelableExtra<Location>(LocationService.INTENT_MESSAGE)
                val resource = googleViewModel.getLocationFromGeoCoordinates(location)
                handleRequestedData(resource, ::requestSuccessHandlerLocationRequest)
            }
        }
    }

    private fun requestSuccessHandlerLocationRequest(locationName: String?) {
        val searchRoutesUrl = socialRoutingApplication
            .getSocialRoutingRootResource()
            .routeSearchUrl
            .split("?")[0]
        currentLocation = locationName!!.split(",")[0]
        val resource = socialRoutingViewModel.searchRoutes(searchRoutesUrl, currentLocation)
        handleRequestedData(resource, ::requestSuccessHandlerRouteSearch)
    }

    private fun requestSuccessHandlerRouteSearch(routeCollection: SimplifiedRouteInputCollection?) {
        val routesSearched = routeCollection!!.routes
        if (routesSearched.isEmpty())
            emptySearchRoutesTextView.visibility = View.VISIBLE
        else
            setRecyclerView(routesSearched)
    }

    private fun setRecyclerView(list: List<RouteInput>) {
        val adapter = SearchRoutesAdapter(list, this)
        val layoutManager = LinearLayoutManager(applicationContext)
        routesSearched = list
        cards_recyclerview.layoutManager = layoutManager
        cards_recyclerview.itemAnimator = DefaultItemAnimator()
        cards_recyclerview.adapter = adapter
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

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.navigation, menu)

        val application = this@NavigationActivity.application as SocialRoutingApplication
        val user = application.getUser()
        name_account_textview.text = user.name
        email_account_textview.text = user.email

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_route_creation -> {
                startNewActivity(RouteCreationActivity::class.java, false)
            }
            R.id.nav_user_profile -> {
                startNewActivity(UserProfileActivity::class.java, false)
            }
            R.id.nav_share -> {

            }
            R.id.nav_send -> {

            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun searchRoutesOnClick(view: View) {
        if (location_editText.text.isEmpty()) {
            val missingLocationMessage = getString(R.string.fill_location_form)
            showToast(missingLocationMessage)
            return
        }
        val intentMessage = getString(R.string.location_intent_message)
        val intent = Intent(this, RoutesSearchActivity::class.java)
        intent.putExtra(intentMessage, location_editText.text.toString())
        startActivity(intent)
    }
}
