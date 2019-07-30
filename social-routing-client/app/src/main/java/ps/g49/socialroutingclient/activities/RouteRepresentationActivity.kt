package ps.g49.socialroutingclient.activities

import android.Manifest
import android.content.Context
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
import com.google.android.gms.location.LocationServices
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationSettingsStatusCodes
import android.content.IntentSender
import android.widget.*
import androidx.appcompat.app.AlertDialog
import ps.g49.socialroutingclient.model.domainModel.Point
import java.util.*

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
    private var modeOfTransport: String = ""

    companion object {
        const val ROUTE_REPRESENTATION_DETAILS_MESSAGE = "ROUTE_REPRESENTATION_DETAILS_MESSAGE"
        const val TIME_TO_CHECK_LOCATION = 2000
        const val DEFAULT_MODE_OF_TRANSPORT = "walking"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove notification bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_route_representation)
        initView()

        socialRoutingApplication = application as SocialRoutingApplication
        socialRoutingViewModel = getViewModel(viewModelFactory)
        googleViewModel = getViewModel(viewModelFactory)

        routeUrl = intent.getStringExtra(getString(R.string.route_creation_intent_message))
    }

    override fun initView() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /*
    * Manipulates the map once available.
    * This callback is triggered when the map is ready to be used.
    * This is where we can add markers or lines, add listeners or move the camera.
    */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        googleMapsManager = GoogleMapsManager(mMap)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
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
        handleRequestedData(liveData, ::successHandlerPlaceOfInterest)
    }

    private fun successHandlerPlaceOfInterest(placeDetailsResponse: PlaceDetailsResponse?) {
        val details = placeDetailsResponse!!.results
        if (details != null)
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
        // Ask user to turn on GPS
        tryToTurnOnGPS()
        // show Initial Form
        showFormToTransportMode()
        // Show path to route
        val currentLocation = socialRoutingApplication.getUserCurrentLocation()
        val currentPoint = Point(currentLocation.latitude, currentLocation.longitude)

        val pointToGo =
            if (route.circular)
                findClosestPoint(currentPoint, route.points)
            else
                route.points.first()

        val liveData = googleViewModel.getDirections(
            currentPoint,
            pointToGo,
            modeOfTransport
        )
        handleRequestedData(liveData, ::successHandlerDirections, ::errorHandlerDirections)
        // Start Async Timer Task
        asyncTimerTaskLocation(pointToGo, route.points, route.ordered, this)

        liveTrackingButton.visibility = View.INVISIBLE
    }

    private fun tryToTurnOnGPS() {
        val googleApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(getConnectionCallback())
            .addOnConnectionFailedListener {
                showToast("Turn on GPS, to be possible to help you.")
            }.build()
        googleApiClient.connect()

        val locationRequest = LocationRequest.create()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5 * 1000
        locationRequest.fastestInterval = 2 * 1000
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        builder.setAlwaysShow(true);

        val result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build())
        result.setResultCallback {
            val status = it.status
            //                final LocationSettingsStates state = result.getLocationSettingsStates();

            when (status.statusCode) {
                LocationSettingsStatusCodes.SUCCESS -> {
                }
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED ->
                    // Location settings are not satisfied. But could be fixed by showing the user
                    // a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        status.startResolutionForResult(this, 1000)
                    } catch (e: IntentSender.SendIntentException) {
                        // Ignore the error.
                    }

                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                }
            }
        }
    }

    private fun getConnectionCallback(): GoogleApiClient.ConnectionCallbacks {
        return object : GoogleApiClient.ConnectionCallbacks {
            override fun onConnected(p0: Bundle?) {
            }

            override fun onConnectionSuspended(p0: Int) {
                showToast("Turn on GPS, to be possible to help you.")
            }

        }
    }

    private fun showFormToTransportMode() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater

        val rowView: View = inflater.inflate(R.layout.form_mode_of_transport_to_route, null)
        val drivingRadioButton: RadioButton = rowView.findViewById(R.id.drivingRadioButton)
        val walkingRadioButton: RadioButton = rowView.findViewById(R.id.walkingRadioButton)
        val bicyclingRadioButton: RadioButton = rowView.findViewById(R.id.bicyclingRadioButton)
        val confirmButton: Button = rowView.findViewById(R.id.confirmChoiceButton)

        val alertDialog = builder
            .setView(rowView)
            .setCancelable(false)
            .create()

        confirmButton.setOnClickListener {
            if (!drivingRadioButton.isChecked && !walkingRadioButton.isChecked && !bicyclingRadioButton.isChecked)
                showToast("Choose the mode of transport first.")
            else {
                modeOfTransport = when {
                    drivingRadioButton.isChecked -> drivingRadioButton.text.toString()
                    walkingRadioButton.isChecked -> walkingRadioButton.text.toString()
                    bicyclingRadioButton.isChecked -> bicyclingRadioButton.text.toString()
                    else -> DEFAULT_MODE_OF_TRANSPORT
                }
                alertDialog.cancel()
            }
        }

        alertDialog.show()
    }

    private fun findClosestPoint(
        currentPoint: Point,
        points: List<Point>
    ): Point {
        var smallestDistance = Double.MAX_VALUE
        var closestPoint: Point? = null
        points.forEach {
            val sumPowDistance = Math.pow(currentPoint.latitude - it.latitude, 2.0) + Math.pow(currentPoint.longitude - it.longitude,2.0)
            val distance = Math.sqrt(sumPowDistance)
            if (smallestDistance > distance) {
                smallestDistance = distance
                closestPoint = it
            }
        }
        return closestPoint!!
    }

    private fun successHandlerDirections(list: List<Point>?) {
        googleMapsManager.drawLineSetToFollow(list!!)
    }

    private fun errorHandlerDirections() {
        showToast("Way to Route not found, sorry!")
    }

    private fun asyncTimerTaskLocation(
        pointToGo: Point,
        points: List<Point>,
        isOrdered: Boolean,
        context: Context
    ) {
        val timer = Timer()
        val timerTask = object : TimerTask() {
            val pointsPassed = mutableListOf<Point>()

            override fun run() {
                // ver dois pontos
                //fazer reta e confirmar se interseta o range do user
                val currentLocation = socialRoutingApplication.getUserCurrentLocation()
                val cuurPoint = Point(currentLocation.latitude, currentLocation.longitude)
                if ( findClosestPoint(cuurPoint, points).equals(points.last()) ) {
                    finish()
                }
            }
        }
        timer.schedule(timerTask, TIME_TO_CHECK_LOCATION.toLong())
    }

}
