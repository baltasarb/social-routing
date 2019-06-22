package ps.g49.socialroutingclient.webService

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.OkHttpClient
import ps.g49.socialroutingclient.webService.interceptors.HeaderInterceptor
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

class RetrofitClient(private val baseUrl: String) {

    private fun getMapper() = jacksonObjectMapper()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)

    fun getBasicClient(): Retrofit {
        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(HeaderInterceptor())
            .build()

        return Retrofit
            .Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(getMapper()))
            .build()
    }

    fun getAuthenticatedClient(accessToken: String): Retrofit {
        val client = OkHttpClient()
            .newBuilder()
            .addInterceptor(HeaderInterceptor(accessToken))
            .build()

        return Retrofit
            .Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(getMapper()))
            .build()
    }

}