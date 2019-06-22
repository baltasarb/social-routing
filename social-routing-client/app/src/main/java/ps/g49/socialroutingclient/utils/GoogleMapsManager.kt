package ps.g49.socialroutingclient.utils

import android.graphics.Color
import ps.g49.socialroutingclient.model.Point
import ps.g49.socialroutingclient.repositories.GoogleRepository
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.*
import java.util.*

class GoogleMapsManager(
    val googleMap: GoogleMap,
    val geocodingRepository: GoogleRepository
) {

    private val markerOptions = LinkedList<Marker>()
    private val polylines = LinkedList<Polyline>()
    private val polylineToFollow = LinkedList<Polyline>()

    companion object {
        private const val START = "Start"
        private const val CITY_ZOOM = 13F
        private const val MARKER_ZOOM = 16F
        private const val POLYLINE_WIDTH = 16F
        private const val LOCATION_CLOSE_ZOOM = 20f
        private const val POLYLINE_FOLLOW_WIDTH = 12F
    }

    fun onMapClickListener(): GoogleMap.OnMapClickListener {
        return GoogleMap.OnMapClickListener {
            addMarkerOptions(it)
        }
    }

    private fun addMarkerOptions(position: LatLng) {
        val markerOption: MarkerOptions

        if (markerOptions.isEmpty())
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

        centerCameraInFinalMarker()
    }

    private fun drawLine(position1: LatLng, position2: LatLng) {
        val polylineOption = PolylineOptions()
            .add(position1, position2)
            .width(16F)
            .color(Color.BLUE)
            .clickable(true)

        polylines.addLast(googleMap.addPolyline(polylineOption))
    }

    fun removeLastMarkerOption() {

        if (markerOptions.isNotEmpty())
            markerOptions.removeLast().remove()

        if (polylines.isNotEmpty())
            polylines.removeLast().isVisible = false

        centerCameraInFinalMarker()
    }

    private fun centerCameraInFinalMarker() {
        if (markerOptions.isNotEmpty()) {
            val lastMarkerPosition = markerOptions.last().position
            val cameraZoom = googleMap.cameraPosition.zoom

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMarkerPosition, cameraZoom))
        }
    }

    private fun centerCameraInInitialMarker() {
        if (markerOptions.isNotEmpty()) {
            val lastMarkerPosition = markerOptions.first().position
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lastMarkerPosition, MARKER_ZOOM))
        }
    }

    private fun moveCameraToCoordinates(latitude: Double, longitude: Double, cameraZoom: Float) {
        val latLng = LatLng(latitude, longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, cameraZoom))
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

    fun drawLinesSet(points: List<Point>) {
        if (points.isNotEmpty()) {
            val firstPoint = points[0]
            val lastPoint = points[points.size - 1]

            val firstMarker = MarkerOptions()
                .position(LatLng(firstPoint.latitude, firstPoint.longitude))
                .title("Initial Point")
            val lastMarker = MarkerOptions()
                .position(LatLng(lastPoint.latitude, lastPoint.longitude))
                .title("Final Point")

            val initialMarker = googleMap.addMarker(firstMarker)
            val finalMarker = googleMap.addMarker(lastMarker)

            markerOptions.add(initialMarker)
            markerOptions.add(finalMarker)

            val polylineOptions = PolylineOptions()
            polylineOptions.width(POLYLINE_WIDTH)
            polylineOptions.color(Color.BLUE)
            polylineOptions.addAll(points.map {
                LatLng(it.latitude, it.longitude)
            })

            val polyline = googleMap.addPolyline(polylineOptions)
            polylines.add(polyline)

            centerCameraInInitialMarker()
        }
    }

    fun drawLineSetToFollow(points: List<Point>) {
        if (points.isNotEmpty()) {
            val polylineOptions = PolylineOptions()

            polylineOptions.addAll(points.map {
                LatLng(it.latitude, it.longitude)
            })
            polylineOptions.width(POLYLINE_FOLLOW_WIDTH)
            polylineOptions.color(Color.GRAY)
            polylineOptions.geodesic(true)

            val polyline = googleMap.addPolyline(polylineOptions)
            polylineToFollow.add(polyline)

            val firstPoint = points[0]
            centerCameraInLocation(firstPoint.latitude, firstPoint.longitude)
        }
    }

    private fun centerCameraInLocation(latitude: Double, longitude: Double) {
        val latLng = LatLng(latitude, longitude)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, LOCATION_CLOSE_ZOOM))
    }
}