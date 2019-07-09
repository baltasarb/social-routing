package ps.g49.socialroutingclient.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import kotlinx.android.synthetic.main.activity_route_representation.*
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.SocialRoutingApplication
import ps.g49.socialroutingclient.dagger.factory.ViewModelFactory
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.domainModel.Point
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
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var mMap: GoogleMap
    private lateinit var route: RouteDetailedInput
    private lateinit var routeUrl: String
    private var routeId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Remove notification bar
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        setContentView(R.layout.activity_route_representation)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        val routeIntentMessage = getString(R.string.route_intent_message)
        val routeIdIntentMessage = getString(R.string.route_id_intent_message)
        routeUrl = intent.getStringExtra(routeIntentMessage)
        routeId = intent.getIntExtra(routeIdIntentMessage, -1)
        socialRoutingApplication = application as SocialRoutingApplication
        socialRoutingViewModel = getViewModel(viewModelFactory)
        googleViewModel = getViewModel(viewModelFactory)
    }

    /*
    * Manipulates the map once available.
    * This callback is triggered when the map is ready to be used.
    * This is where we can add markers or lines, add listeners or move the camera.
    */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        googleMapsManager = GoogleMapsManager(mMap)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mMap.isMyLocationEnabled = true
        else
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST)

        val apiUrl = getString(R.string.api_url)
        val correctRouteUrl = apiUrl + routeUrl.split("/")[1] + "s/" +  routeId
        val liveData = socialRoutingViewModel.getRoute(correctRouteUrl)
        handleRequestedData(liveData, ::requestSuccessHandlerRouteRepresentation, ::requestErrorHandlerRouteRepresentation)

        val locationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                locationResult ?: return
                for (location in locationResult.locations){
                    // Update UI with location data
                }
            }
        }
        fusedLocationProviderClient.requestLocationUpdates(LocationRequest(), locationCallback, null)
    }

    private fun requestErrorHandlerRouteRepresentation(errorMessage: String?) {
        showToast(errorMessage!!)
        liveTrackingButton.visibility = View.INVISIBLE
    }

    private fun requestSuccessHandlerRouteRepresentation(routeDetailed: RouteDetailedInput?) {
        val points = routeDetailed!!.points
        route = routeDetailed
        googleMapsManager.drawLinesSet(points)
    }

    fun liveTrackingOnClick(view: View) {
        //showInitialForm()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationTask = fusedLocationProviderClient.lastLocation
            locationTask.addOnSuccessListener {
                val initialPoint = Point(it.latitude, it.longitude)

                // TODO( "Change mode of transport to be a user choice
                val liveData = googleViewModel.getDirections(
                    initialPoint,
                    route.points.first(),
                    "walking"
                )
                handleRequestedData(liveData, ::successRequestHandlerGoogleDirection)
            }
        }
        liveTrackingButton.visibility = View.INVISIBLE
    }

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
    }
}
