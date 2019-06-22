package ps.g49.socialroutingclient.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ps.g49.socialroutingclient.model.Point
import ps.g49.socialroutingclient.model.inputModel.directions.DirectionsResponse
import ps.g49.socialroutingclient.model.inputModel.geocoding.GeoCodingResponse
import ps.g49.socialroutingclient.model.inputModel.geocoding.PointGeocoding
import ps.g49.socialroutingclient.utils.GoogleOverviewPolylineDecoder
import ps.g49.socialroutingclient.utils.Resource
import ps.g49.socialroutingclient.webService.GoogleWebService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleRepository @Inject constructor(
    val googleWebService: GoogleWebService
) {

    fun getGeoCoordinatesFromLocation(location: String, googleMapsKey: String): LiveData<Resource<PointGeocoding>> {
        val resource = MutableLiveData<Resource<PointGeocoding>>()
        resource.value = Resource.loading()

        googleWebService
            .getGeocode(location, googleMapsKey)
            .enqueue(object : Callback<GeoCodingResponse> {

                override fun onFailure(call: Call<GeoCodingResponse>, t: Throwable) {
                    resource.value = Resource.error(t.message.toString(), null)
                }

                override fun onResponse(call: Call<GeoCodingResponse>, response: Response<GeoCodingResponse>) {
                    if (response.isSuccessful && response.code() == 200) {
                        val geoCodingResponse = response.body()
                        if (geoCodingResponse!!.results.isNotEmpty()) {
                            val point = geoCodingResponse.results.first().geometry.location
                            resource.value = Resource.success(point)
                        }
                    }
                    resource.value = Resource.error(response.errorBody().toString(), null)
                }

            })

        return resource
    }

    fun getDirections(
        initialLocation: Point,
        destinationLocation: Point,
        modeOfTransport: String,
        googleMapsKey: String
    ): LiveData<Resource<List<Point>>> {
        val resource = MutableLiveData<Resource<List<Point>>>()
        resource.value = Resource.loading()

        val initialLocationString = initialLocation.latitude.toString() + "," + initialLocation.longitude.toString()
        val destinationLocationString =
            destinationLocation.latitude.toString() + "," + destinationLocation.longitude.toString()

        val call = googleWebService
            .getDirections(
                initialLocationString,
                destinationLocationString,
                modeOfTransport,
                googleMapsKey
            )
            .enqueue(object : Callback<DirectionsResponse> {
                override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                    resource.value = Resource.error(t.message.toString(), null)
                }

                override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                    if (response.isSuccessful && response.code() == 200) {
                        val directionsResponse = response.body()!!
                        if (directionsResponse.status == "REQUEST_DENIED") {
                            resource.value = Resource.error("Could not find the directions to the route, sorry...", null)
                            return
                        }
                        val encodedPoints = directionsResponse.routes!!.get(0).overview_polyline.points
                        val points = GoogleOverviewPolylineDecoder.googleOverviewPolylineDecode(encodedPoints)
                        resource.value = Resource.success(points)
                        return
                    }
                    resource.value = Resource.error(response.errorBody().toString(), null)
                }
            })

        return resource
    }

}
