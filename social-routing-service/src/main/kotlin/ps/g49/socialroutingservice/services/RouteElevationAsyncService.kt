package ps.g49.socialroutingservice.services

import com.google.maps.ElevationApi
import com.google.maps.GeoApiContext
import com.google.maps.model.LatLng
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.models.domainModel.Point
import ps.g49.socialroutingservice.repositories.RouteRepository


/**
 * This service communicates with the GoogleElevation API  in an asynchronous fashion
 */
@Service
class RouteElevationAsyncService (
        private val routeRepository: RouteRepository
){

    companion object {
        private const val API_KEY = "AIzaSyDT4mgRcDFGiwTdkrZ3tZWp4s-rgJ-xvoQ"

        private val context = GeoApiContext
                .Builder()
                .apiKey(API_KEY)
                .build()

        private const val NUMBER_OF_SAMPLES = 256
    }

    fun findElevation(routeIdentifier: Int, points: List<Point>) {
        val latLngList: List<LatLng> = points.map { LatLng(it.latitude, it.longitude) }
        val samples = latLngList.size //todo test and change
        val elevationResultArray = ElevationApi.getByPath(context, samples , *latLngList.toTypedArray()).await()
        //todo improve the accumulated calculation

        val elevation = elevationResultArray
                .map{it.elevation} //transform to a list of elevation values
                .reduce{acc, it -> acc + it} //sum the values

        routeRepository.updateElevation(routeIdentifier, elevation)
    }

}