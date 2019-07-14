package ps.g49.socialroutingclient.interceptors

import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.Response

class HeaderInterceptor(private val accessToken: String? = null) : Interceptor {

    override fun intercept(chain: Chain): Response {
        val initialRequest = chain.request()
        val headersBuilder = Headers.Builder()

        if (initialRequest.method() == "POST" || initialRequest.method() == "PUT")
            headersBuilder.add("Content-Type", "application/json")

        if (accessToken != null)
            headersBuilder.add("Authorization", accessToken)

        val headers = headersBuilder.build()
        val finalRequest = initialRequest
            .newBuilder()
            .headers(headers)
            .build()

        return chain.proceed(finalRequest)
    }

}
