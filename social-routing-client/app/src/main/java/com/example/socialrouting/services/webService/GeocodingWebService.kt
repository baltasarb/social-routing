package com.example.socialrouting.services.webService

import com.example.socialrouting.model.inputModel.geocoding.GeoCodingResponse
import retrofit2.Call
import retrofit2.http.*

interface GeocodingWebService {

    @GET("geocode/json")
    fun getGeocode(@Query("address") address: String, @Query("key") key: String): Call<GeoCodingResponse>

}