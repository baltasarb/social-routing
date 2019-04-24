package com.example.socialrouting.utils

import com.example.socialrouting.model.Point
import com.example.socialrouting.repositories.GeocodingRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import java.util.*

class GoogleMapsManager(val googleMap: GoogleMap) {

    private val markerOptions = LinkedList<Marker>()
    private val polylines = LinkedList<Polyline>()
    private val geocodingRepository = GeocodingRepository()

    companion object {
        private const val START = "Start"
        private const val CITY_ZOOM = 13F
    }

    fun onMapClickListener(): GoogleMap.OnMapClickListener {
        return GoogleMap.OnMapClickListener {
            addMarkerOptions(it)
        }
    }

    private fun addMarkerOptions(position: LatLng) {
        val markerOption: MarkerOptions

        if (markerOptions.size == 0)
            markerOption = MarkerOptions()
                .position(position)
                .title(START)
        else {
            markerOption = MarkerOptions()
                .position(position)
                .title(String.format("Sub-path %d", markerOptions.size))

            drawLine(markerOptions.last.position, position)
        }

        val marker = googleMap.addMarker(markerOption)
        markerOptions.addLast(marker)

        centerCamera()
    }

    private fun drawLine(position1: LatLng, position2: LatLng) {
        val polylineOption = PolylineOptions()
            .add(position1, position2)
            .clickable(true)

        polylines.addLast(googleMap.addPolyline(polylineOption))
    }

    fun removeLastMarkerOption() {

        if (markerOptions.isNotEmpty())
            markerOptions.removeLast().remove()

        if (polylines.isNotEmpty())
            polylines.removeLast().isVisible = false

        centerCamera()
    }

    private fun centerCamera() {
        if (markerOptions.isNotEmpty()) {
            val lastMarkerPosition = markerOptions.last().position
            val cameraZoom = googleMap.cameraPosition.zoom

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMarkerPosition, cameraZoom))
        }
    }

    private fun moveCameraToCoordinates(latitude: Double, longitude: Double, cameraZoom: Float) {
        val latLng = LatLng(latitude, longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, cameraZoom))
    }

    fun setMapUISettings() {
        val settings = googleMap.uiSettings
        settings.isZoomControlsEnabled = true
        // googleMap.isMyLocationEnabled = true
        // ToDo Permissions necessary(https://stackoverflow.com/questions/40142331/how-to-request-location-permission-at-runtime)
    }

    fun zoomInLocation(location: String, errorResponse: (str: String) -> Unit) {
        geocodingRepository.getGeoCoordinatesFromLocation(location, { lat, long ->
            moveCameraToCoordinates(lat, long, CITY_ZOOM)
        }, errorResponse)
    }

    fun getMarkerPoints(): List<Point> = markerOptions.map {
        val latLng = it.position
        Point(latLng.latitude, latLng.longitude)
    }

    fun mapIsMarked() = markerOptions.isNotEmpty()

}