package ps.g49.socialroutingclient.activities

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import ps.g49.socialroutingclient.R
import ps.g49.socialroutingclient.kotlinx.getViewModel
import ps.g49.socialroutingclient.model.inputModel.RouteDetailedInput
import ps.g49.socialroutingclient.utils.GoogleMapsManager
import ps.g49.socialroutingclient.viewModel.RouteViewModel

class RouteRepresentationActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var googleMapsManager: GoogleMapsManager
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var routeViewModel: RouteViewModel
    private lateinit var mMap: GoogleMap
    private var routeId: Int = -1
    val PERMISSIONS = arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
    val LOCATION_PERMISSION_REQUEST = 1234

    companion object{
        const val ROUTE_ID_MESSAGE = "ROUTE_ID_MESSAGE"
        const val REQUEST_CODE = 101
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
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        routeId = intent.getIntExtra(ROUTE_ID_MESSAGE, -1)
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
        routeViewModel = getViewModel()

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            mMap.isMyLocationEnabled = true
        else {
            // Show rationale and request permission.
            ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST)
        }

        val liveData = routeViewModel.getRoute(routeId)
        handleRequestedData(liveData, ::requestSuccessHandlerRouteRepresentation)
    }

    fun requestSuccessHandlerRouteRepresentation(routeDetailed: RouteDetailedInput) {
        val points = routeDetailed.points
        googleMapsManager.drawLinesSet(points)
    }
}