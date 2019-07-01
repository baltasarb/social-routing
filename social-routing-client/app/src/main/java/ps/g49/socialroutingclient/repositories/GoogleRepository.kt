package ps.g49.socialroutingclient.repositories

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ps.g49.socialroutingclient.model.Point
import ps.g49.socialroutingclient.model.inputModel.geocoding.PointGeocoding
import ps.g49.socialroutingclient.utils.GoogleOverviewPolylineDecoder
import ps.g49.socialroutingclient.utils.Resource
import ps.g49.socialroutingclient.webService.GoogleWebService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleRepository @Inject constructor(
    val googleWebService: GoogleWebService
) : BaseRepository() {

    companion object {
        const val googleMapsKey = "AIzaSyCpwLrcZPuDfuuDBRDKasrPAzviHiyc4N8"
    }

    fun getGeocoding(location: String): LiveData<Resource<PointGeocoding>> {
        val resource = MutableLiveData<Resource<PointGeocoding>>()
        resource.value = Resource.loading()

        val call = googleWebService.getGeocode(location, googleMapsKey)
        genericEnqueue(call, resource) {
            it.results.first().geometry.location
        }

        return resource
    }

    fun getReverseGeocoding(location: Location): LiveData<Resource<String>> {
        val resource = MutableLiveData<Resource<String>>()
        resource.value = Resource.loading()

        val locationsStr = location.latitude.toString() + "," + location.longitude
        val call = googleWebService.getReverseGeocode(locationsStr, googleMapsKey)
        genericEnqueue(call, resource) {
            val results = it.results
            val addressComponents = results.first().addressComponents
            addressComponents.find {
                it.types.contains("locality")
            }!!.long_name
        }

        return resource
    }

    fun getDirections(
        initialLocation: Point,
        destinationLocation: Point,
        modeOfTransport: String
    ): LiveData<Resource<List<Point>>> {

        val resource = MutableLiveData<Resource<List<Point>>>()
        resource.value = Resource.loading()

        val initialLocationString = initialLocation.latitude.toString() + "," + initialLocation.longitude.toString()
        val destinationLocationString =
            destinationLocation.latitude.toString() + "," + destinationLocation.longitude.toString()

        val call = googleWebService.getDirections(
            initialLocationString,
            destinationLocationString,
            modeOfTransport,
            googleMapsKey
        )
        genericEnqueue(call, resource) {
            val encodedPoints = it.routes!!.get(0).overview_polyline.points
            GoogleOverviewPolylineDecoder.googleOverviewPolylineDecode(encodedPoints)
        }

        return resource
    }

}
