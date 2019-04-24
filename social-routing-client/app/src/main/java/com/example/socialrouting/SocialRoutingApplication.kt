package com.example.socialrouting

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import com.example.socialrouting.services.webService.GeocodingWebService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class SocialRoutingApplication : Application() {

    lateinit var token: String
    lateinit var queue: RequestQueue
    lateinit var geocodingService: GeocodingWebService

    override fun onCreate() {
        super.onCreate()

        queue = Volley.newRequestQueue(this)

        initGeocodingRetrofit()
    }


    fun initGeocodingRetrofit() {
        val retrofitGeocoding = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(JacksonConverterFactory.create())
            .build()

        geocodingService = retrofitGeocoding.create(GeocodingWebService::class.java)
    }

    fun getGoogleMapsKey() = "AIzaSyCpwLrcZPuDfuuDBRDKasrPAzviHiyc4N8"
}