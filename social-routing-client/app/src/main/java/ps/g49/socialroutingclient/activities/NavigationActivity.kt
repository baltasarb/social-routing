package ps.g49.socialroutingclient.activities

import android.content.Intent
import android.location.Location
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
import android.widget.CheckBox
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.nav_header_navigation.*
import kotlinx.android.synthetic.main.search_content_navigation_layout.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.adapters.SearchRoutesAdapter
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.inputModel.socialRouting.RouteInput
import ps.g49.socialroutingclient.model.inputModel.socialRouting.SimplifiedRouteInputCollection
import ps.g49.socialroutingclient.adapters.OnRouteListener
import ps.g49.socialroutingclient.model.domainModel.Category
import ps.g49.socialroutingclient.model.domainModel.Point
import ps.g49.socialroutingclient.model.domainModel.Search
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.reverse.ReverseGeoCodingResponse
import ps.g49.socialroutingclient.model.inputModel.socialRouting.CategoryCollectionInput
import ps.g49.socialroutingclient.utils.ScrollListener
import ps.g49.socialroutingclient.viewModel.GoogleViewModel
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import javax.inject.Inject

class NavigationActivity : BaseActivity(), NavigationView.OnNavigationItemSelectedListener, OnRouteListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var googleViewModel: GoogleViewModel
    private lateinit var socialRoutingViewModel: SocialRoutingViewModel
    private lateinit var socialRoutingApplication: SocialRoutingApplication
    private lateinit var routesSearched: List<RouteInput>
    private lateinit var categories: List<Category>
    private var nextPage: String? = null

    companion object {
        const val DEFAULT_DURATION = "medium"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_navigation)
        initView()

        socialRoutingApplication = application as SocialRoutingApplication
        googleViewModel = getViewModel(viewModelFactory)
        socialRoutingViewModel = getViewModel(viewModelFactory)

        requestCategories()
    }

    private fun requestCategories() {
        categories = listOf()
        val categoriesUrl = socialRoutingApplication.getSocialRoutingRootResource().categoriesUrl
        val liveData = socialRoutingViewModel.getRouteCategories(categoriesUrl)
        handleRequestedData(liveData, ::requestSuccessHandlerCategories)
    }

    private fun requestSuccessHandlerCategories(categoryCollection: CategoryCollectionInput?) {
        categories = categoryCollection!!.categories
    }

    private fun initView() {
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

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
        sliding_layout.isEnabled = false
        stopSpinner()
    }

    override fun onStart() {
        super.onStart()
        if (socialRoutingApplication.isLocationFound())
            getUserLocationName(socialRoutingApplication.getUserCurrentLocation())
        else emptySearchRoutesNavigationTextView.visibility = View.VISIBLE
    }

    private fun getUserLocationName(location: Location) {
        val resource = googleViewModel.getLocationFromGeoCoordinates(location)
        handleRequestedData(resource, ::successHandlerLocationRequest, ::errorHandler)
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

        val user = socialRoutingApplication.getUser()
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
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun successHandlerLocationRequest(reverseGeoCodingResponse: ReverseGeoCodingResponse?) {
        val searchRoutesUrl = socialRoutingApplication
            .getSocialRoutingRootResource()
            .routeSearchUrl
        val result = reverseGeoCodingResponse!!.results.first {
            it.types.contains("locality") && it.types.contains("political")
        }
        val locationIdentifier = result.placeId
        val locationPoint = result.geometry.location
        val resource = socialRoutingViewModel.searchRoutes(
            searchRoutesUrl,
            locationIdentifier,
            categories,
            DEFAULT_DURATION,
            Point(locationPoint.lat, locationPoint.lng)
        )
        handleRequestedData(resource, ::requestSuccessHandlerRouteSearch, ::errorHandler)
    }

    private fun requestSuccessHandlerRouteSearch(routeCollection: SimplifiedRouteInputCollection?) {
        emptySearchRoutesNavigationTextView.visibility = View.GONE
        val routesSearched = routeCollection!!.routes
        nextPage = routeCollection.next
        if (routesSearched.isEmpty())
            emptySearchRoutesNavigationTextView.visibility = View.VISIBLE
        else {
            setRecyclerView(routesSearched)
        }
    }

    private fun errorHandler() {
        emptySearchRoutesNavigationTextView.visibility = View.VISIBLE
    }

    private fun setRecyclerView(list: List<RouteInput>) {
        routesSearched = list
        val adapter = SearchRoutesAdapter(this, routesSearched, this, false)
        val layoutManager = LinearLayoutManager(applicationContext)
        cards_recyclerview.layoutManager = layoutManager
        cards_recyclerview.itemAnimator = DefaultItemAnimator()
        cards_recyclerview.adapter = adapter
        cards_recyclerview.addOnScrollListener(
            ScrollListener {
                TODO("search new page")
            }
        )
    }

    override fun onRouteClick(position: Int) {
        if (routesSearched.isNotEmpty()) {
            val routeIntentMessage = getString(R.string.route_creation_intent_message)
            val routeUrl = socialRoutingApplication.setCorrectUrlToDevice(routesSearched[position].routeUrl)

            val intent = Intent(this, RouteRepresentationActivity::class.java)
            intent.putExtra(routeIntentMessage, routeUrl)
            startActivity(intent)
        }
    }

    fun searchRoutesOnClick(view: View) {
        val locationName = location_editText.text.toString()
        val categoriesChecked = mutableListOf<Category>()
        for (idx in 0 until categoriesLinearLayout.childCount) {
            val checkBox = categoriesLinearLayout.getChildAt(idx) as CheckBox
            if (checkBox.isChecked)
                categoriesChecked.add(categories[idx])
        }

        val duration = when {
            lowRadioButton.isChecked -> lowRadioButton.text.toString()
            mediumRadioButton.isChecked -> mediumRadioButton.text.toString()
            else -> "long"
        }

        val searchParams = Search(
            locationName,
            categoriesChecked,
            duration
        )

        val intentMessage = getString(R.string.location_intent_message)
        val intent = Intent(this, RoutesSearchActivity::class.java)
        intent.putExtra(intentMessage, searchParams)
        startActivity(intent)
    }

    fun addSearchFilters(view: View) {
        if (location_editText.text.isEmpty()) {
            val missingLocationMessage = getString(R.string.fill_location_form)
            showToast(missingLocationMessage)
            return
        }

        sliding_layout.isEnabled = true
        sliding_layout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED

        if (categoriesLinearLayout.childCount > 0)
            categoriesLinearLayout.removeAllViews()

        categories.forEach {
            val checkBox = CheckBox(this)
            checkBox.text = it.name
            categoriesLinearLayout.addView(checkBox)
        }

    }
}
