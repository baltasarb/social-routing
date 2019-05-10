package com.example.socialrouting.activities

import android.os.Bundle
import com.example.socialrouting.R
import com.example.socialrouting.kotlinx.getViewModel
import com.example.socialrouting.model.inputModel.RouteDetailedInput
import com.example.socialrouting.utils.GoogleMapsManager
import com.example.socialrouting.viewModel.RouteViewModel
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class RouteRepresentationActivity : BaseActivity(), OnMapReadyCallback {

    private lateinit var googleMapsManager: GoogleMapsManager
    private lateinit var routeViewModel: RouteViewModel
    private lateinit var mMap: GoogleMap
    private var routeId: Int = -1

    companion object{
        const val ROUTE_ID_MESSAGE = "ROUTE_ID_MESSAGE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

        googleMapsManager = GoogleMapsManager(mMap)
        routeViewModel = getViewModel()
        val liveData = routeViewModel.getRoute(routeId)
        handleRequestedData(liveData)
    }

    override fun <T> requestSuccessHandler(result: T) {
        val routeDetailed = result as RouteDetailedInput
        val points = routeDetailed.points.points
        googleMapsManager.drawLinesSet(points)
    }
}
