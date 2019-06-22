package ps.g49.socialroutingclient.webService

import ps.g49.socialroutingclient.model.inputModel.directions.DirectionsResponse
import ps.g49.socialroutingclient.model.inputModel.geocoding.GeoCodingResponse
import retrofit2.Call
import retrofit2.http.*

interface GoogleWebService {

    @GET("geocode/json")
    fun getGeocode(
        @Query("address") address: String,
        @Query("key") key: String
    ): Call<GeoCodingResponse>

    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("key") key: String
    ): Call<DirectionsResponse>

}