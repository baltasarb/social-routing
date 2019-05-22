package ps.g49.socialroutingclient.repositories

import ps.g49.socialroutingclient.model.inputModel.geocoding.GeoCodingResponse
import ps.g49.socialroutingclient.webService.GeocodingWebService
import ps.g49.socialroutingclient.webService.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GeocodingRepository {

    companion object {
        const val baseUrl = "https://maps.googleapis.com/maps/api/"
        const val getGoogleMapsKey = "AIzaSyCpwLrcZPuDfuuDBRDKasrPAzviHiyc4N8"
    }

    private val retrofitGeocoding = RetrofitClient(baseUrl).getClient()
    val geocoding = retrofitGeocoding.create(GeocodingWebService::class.java)

    fun getGeoCoordinatesFromLocation(location: String, success: (Double, Double) -> Unit, errorScenario: (String) -> Unit) {
        val call= geocoding.getGeocode(location, getGoogleMapsKey)
        call.enqueue(object: Callback<GeoCodingResponse> {

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

}
