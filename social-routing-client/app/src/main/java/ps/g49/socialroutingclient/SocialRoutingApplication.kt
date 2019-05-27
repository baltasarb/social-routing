package ps.g49.socialroutingclient

import android.app.Application
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley
import ps.g49.socialroutingclient.webService.GoogleWebService
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class SocialRoutingApplication : Application() {

    lateinit var queue: RequestQueue
    lateinit var googleService: GoogleWebService

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

        googleService = retrofitGeocoding.create(GoogleWebService::class.java)
    }

}