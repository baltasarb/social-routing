package ps.g49.socialroutingclient.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_route_creation.*
import kotlinx.android.synthetic.main.activity_route_representation.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.domainModel.RouteDetails
import ps.g49.socialroutingclient.model.inputModel.google.places.PlaceDetailsResponse
import ps.g49.socialroutingclient.model.inputModel.socialRouting.RouteDetailedInput
import ps.g49.socialroutingclient.utils.GoogleMapsManager
import ps.g49.socialroutingclient.viewModel.GoogleViewModel
import ps.g49.socialroutingclient.viewModel.SocialRoutingViewModel
import javax.inject.Inject

class RouteRepresentationActivity : BaseActivity(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var socialRoutingApplication: SocialRoutingApplication
    private lateinit var socialRoutingViewModel: SocialRoutingViewModel
    private lateinit var googleViewModel: GoogleViewModel
    private lateinit var googleMapsManager: GoogleMapsManager
    private lateinit var mMap: GoogleMap
    private lateinit var route: RouteDetailedInput
    private lateinit var routeUrl: String

    companion object {
        const val ROUTE_REPRESENTATION_DETAILS_MESSAGE = "ROUTE_REPRESENTATION_DETAILS_MESSAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove notification bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_route_representation)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        socialRoutingApplication = application as SocialRoutingApplication
        socialRoutingViewModel = getViewModel(viewModelFactory)
        googleViewModel = getViewModel(viewModelFactory)

        routeUrl = intent.getStringExtra(getString(R.string.route_creation_intent_message))
    }

    /*
    * Manipulates the map once available.
    * This callback is triggered when the map is ready to be used.
    * This is where we can add markers or lines, add listeners or move the camera.
    */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMapsManager = GoogleMapsManager(mMap)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mMap.isMyLocationEnabled = true
        else
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST)

        val liveData = socialRoutingViewModel.getRoute(routeUrl)
        handleRequestedData(liveData, ::successHandlerRouteRepresentation, ::errorHandlerRouteRepresentation)
    }

    private fun errorHandlerRouteRepresentation() {
        routeInfoButton.visibility = View.GONE
        showToast("Could not find the route.")
        liveTrackingButton.visibility = View.INVISIBLE
    }

    private fun successHandlerRouteRepresentation(routeDetailed: RouteDetailedInput?) {
        route = routeDetailed!!
        val points = routeDetailed.points
        // Draw Route
        googleMapsManager.drawLinesSet(points)
        if (routeDetailed.circular)
            googleMapsManager.drawLineSetToFollow(listOf(points.first(), points.last()))
        // Draw Points of Interest
        routeDetailed.pointsOfInterest.forEach {
            requestPointsOfInterestDetails(it.identifier)
        }
    }

    private fun requestPointsOfInterestDetails(placeIdentifier: String) {
        val liveData = googleViewModel.getPlaceDetails(placeIdentifier)
        handleRequestedData(liveData, ::successHandlerPlaceDetails)
    }

    private fun successHandlerPlaceDetails(placeDetailsResponse: PlaceDetailsResponse?) {
        val details = placeDetailsResponse!!.results
        googleMapsManager.addRepresentativeMarkerForPlaces(details.geometry.location, details.name)
    }

    fun routeDetailsOnClick(view: View) {
        val routeDetails = RouteDetails(
            route.categories,
            route.dateCreated.toString(),
            route.name,
            route.description,
            route.imageReference,
            route.rating
        )
        val intent = Intent(this, RouteDetailsActivity::class.java)
        intent.putExtra(ROUTE_REPRESENTATION_DETAILS_MESSAGE, routeDetails)
        startActivity(intent)
    }

    fun liveTrackingOnClick(view: View) {
        //showInitialForm()
        /*if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationTask = fusedLocationProviderClient.lastLocation
            locationTask.addOnSuccessListener {
                val initialPoint = Point(it.latitude, it.longitude)

                // TODO( "Change mode of transport to be a user choice" )
                val liveData = googleViewModel.getDirections(
                    initialPoint,
                    route.points.first(),
                    "walking"
                )
                handleRequestedData(liveData, ::successRequestHandlerGoogleDirection)
            }
        }
        liveTrackingButton.visibility = View.INVISIBLE*/
    }

    /*
    private fun successRequestHandlerGoogleDirection(list: List<Point>?) {
        googleMapsManager.drawLineSetToFollow(list!!)
    }

    private fun showInitialForm() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater

        val rowView = inflater.inflate(R.layout.route_directions_dialog, null)

        builder.setView(rowView)
            .setPositiveButton("Done") { dialog, which ->

            }
            .setNegativeButton("Cancel") { dialog, which -> dialog.cancel() }
            .create()
            .show()
    }*/
}
