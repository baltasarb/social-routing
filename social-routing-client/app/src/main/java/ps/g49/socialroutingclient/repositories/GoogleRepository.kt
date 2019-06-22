package ps.g49.socialroutingclient.repositories

import ps.g49.socialroutingclient.model.Point
import ps.g49.socialroutingclient.model.inputModel.directions.DirectionsResponse
import ps.g49.socialroutingclient.model.inputModel.geocoding.GeoCodingResponse
import ps.g49.socialroutingclient.utils.GoogleOverviewPolylineDecoder
import ps.g49.socialroutingclient.webService.GoogleWebService
import ps.g49.socialroutingclient.webService.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoogleRepository @Inject constructor(
    val webService: GoogleWebService
) {

    companion object {
        const val baseUrl = "https://maps.googleapis.com/maps/api/"
        const val googleMapsKey = "AIzaSyCpwLrcZPuDfuuDBRDKasrPAzviHiyc4N8"
    }

    val retrofitClient = RetrofitClient(baseUrl).getBasicClient()
    val googleWebService = retrofitClient.create(GoogleWebService::class.java)

    fun getGeoCoordinatesFromLocation(
        location: String,
        success: (Double, Double) -> Unit,
        errorScenario: (String) -> Unit
    ) {
        val call = googleWebService.getGeocode(location, googleMapsKey)
        call.enqueue(object : Callback<GeoCodingResponse> {

            override fun onFailure(call: Call<GeoCodingResponse>, t: Throwable) {
                errorScenario(t.message.toString())
            }

            override fun onResponse(call: Call<GeoCodingResponse>, response: Response<GeoCodingResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val geoCodingResponse = response.body()
                    if (geoCodingResponse!!.results.isNotEmpty()) {
                        val point = geoCodingResponse.results.first().geometry.location
                        success(point.lat, point.lng)
                        return
                    }
                }
                errorScenario(response.errorBody().toString())
            }

        })
    }

    fun getDirections(
        initialLocation: Point,
        destinationLocation: Point,
        modeOfTransport: String,
        success: (List<Point>) -> Unit,
        errorScenario: (String) -> Unit
    ) {
        val initialLocationString = initialLocation.latitude.toString() + "," + initialLocation.longitude.toString()
        val destinationLocationString =
            destinationLocation.latitude.toString() + "," + destinationLocation.longitude.toString()

        val call = googleWebService.getDirections(
            initialLocationString,
            destinationLocationString,
            modeOfTransport,
            googleMapsKey
        )
        call.enqueue(object : Callback<DirectionsResponse> {
            override fun onFailure(call: Call<DirectionsResponse>, t: Throwable) {
                errorScenario(t.message.toString())
            }

            override fun onResponse(call: Call<DirectionsResponse>, response: Response<DirectionsResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    val directionsResponse = response.body()!!
                    if (directionsResponse.status == "REQUEST_DENIED") {
                        errorScenario("Could not find the directions to the route, sorry...")
                        return
                    }
                    val encodedPoints = directionsResponse.routes!!.get(0).overview_polyline.points
                    val points = GoogleOverviewPolylineDecoder.googleOverviewPolylineDecode(encodedPoints)
                    success(points)
                    return
                }
                errorScenario(response.errorBody().toString())
            }
        })
    }

}
