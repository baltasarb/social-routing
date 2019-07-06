package ps.g49.socialroutingclient.services.webService

import okhttp3.ResponseBody
import ps.g49.socialroutingclient.model.inputModel.google.directions.DirectionsResponse
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.GeoCodingResponse
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.reverse.ReverseGeoCodingResponse
import ps.g49.socialroutingclient.model.inputModel.google.places.PlacesResponse
import retrofit2.Call
import retrofit2.http.*

interface GoogleWebService {

    @GET("geocode/json")
    fun getGeocode(
        @Query("address") address: String,
        @Query("key") key: String
    ): Call<GeoCodingResponse>

    @GET("geocode/json")
    fun getReverseGeocode(
        @Query("latlng") latlng: String,
        @Query("key") key: String
    ): Call<ReverseGeoCodingResponse>

    @GET("directions/json")
    fun getDirections(
        @Query("origin") origin: String,
        @Query("destination") destination: String,
        @Query("mode") mode: String,
        @Query("key") key: String
    ): Call<DirectionsResponse>

    @GET("place/nearbysearch/json")
    fun findPlacesofInterest(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("key") key: String
    ): Call<PlacesResponse>

    @GET("place/nearbysearch/json")
    fun findPlacesOfInterestNextPage(
        @Query("pagetoken") nextPageToken: String,
        @Query("key") key: String
    ): Call<PlacesResponse>


    @GET("place/photo")
    fun getPhotoFromReference(
        @Query("photoreference") photoReference: String,
        @Query("maxheight") maxHeight: Int,
        @Query("maxwidth") maxWidth: Int,
        @Query("key") key: String
    ): Call<ResponseBody>

}