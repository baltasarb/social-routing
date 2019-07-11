package ps.g49.socialroutingclient.repositories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import okhttp3.ResponseBody
import ps.g49.socialroutingclient.model.domainModel.Point
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.GeoCodingResponse
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.PointGeocoding
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.reverse.ReverseGeoCodingResponse
import ps.g49.socialroutingclient.model.inputModel.google.places.PlaceDetailsResponse
import ps.g49.socialroutingclient.model.inputModel.google.places.PlacesResponse
import ps.g49.socialroutingclient.utils.GoogleOverviewPolylineDecoder
import ps.g49.socialroutingclient.utils.Resource
import ps.g49.socialroutingclient.services.webService.GoogleWebService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleRepository @Inject constructor(
    val googleWebService: GoogleWebService
) : BaseRepository() {

    companion object {
        const val googleMapsKey = "AIzaSyCpwLrcZPuDfuuDBRDKasrPAzviHiyc4N8"
    }

    fun getGeocoding(location: String): LiveData<Resource<GeoCodingResponse>> {
        val resource = MutableLiveData<Resource<GeoCodingResponse>>()
        resource.value = Resource.loading()

        val call = googleWebService.getGeocode(location, googleMapsKey)
        genericEnqueue(call, resource)

        return resource
    }

    fun getReverseGeocoding(location: Location): LiveData<Resource<ReverseGeoCodingResponse>> {
        val resource = MutableLiveData<Resource<ReverseGeoCodingResponse>>()
        resource.value = Resource.loading()

        val locationsStr = location.latitude.toString() + "," + location.longitude
        val call = googleWebService.getReverseGeocode(locationsStr, googleMapsKey)
        genericEnqueue(call, resource)

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

    fun findPlacesOfInterest(
        location: String,
        radius: Int
    ): LiveData<Resource<PlacesResponse>> {

        val resource = MutableLiveData<Resource<PlacesResponse>>()
        resource.value = Resource.loading()

        val call = googleWebService.findPlacesofInterest(location, radius, googleMapsKey)
        genericEnqueue(call, resource)

        return resource
    }

    fun findPlacesOfInterestNextPage(
        pageToken: String
    ): LiveData<Resource<PlacesResponse>> {

        val resource = MutableLiveData<Resource<PlacesResponse>>()
        resource.value = Resource.loading()

        val call = googleWebService.findPlacesOfInterestNextPage(pageToken, googleMapsKey)
        genericEnqueue(call, resource)

        return resource
    }

    fun getPhotoFromReference(
        photoReference: String,
        maxHeight: Int,
        maxWidth: Int
    ): LiveData<Resource<Bitmap>> {

        val resource = MutableLiveData<Resource<Bitmap>>()
        resource.value = Resource.loading()

        val call = googleWebService.getPhotoFromReference(photoReference, maxHeight, maxWidth, googleMapsKey)
        genericEnqueue(call, resource) {
            val body = it.byteStream()
            BitmapFactory.decodeStream(body)
        }

        return resource
    }

    fun getPlaceDetails(placeid: String): LiveData<Resource<PlaceDetailsResponse>> {
        val resource = MutableLiveData<Resource<PlaceDetailsResponse>>()
        resource.value = Resource.loading()

        val call = googleWebService.getPlaceDetails(placeid, googleMapsKey)
        genericEnqueue(call, resource)

        return resource
    }

}
