package com.example.socialrouting.services

import com.android.volley.*
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import org.json.JSONObject
import com.example.socialrouting.SocialRoutingApplication

class SocialRoutingApiService(val application: SocialRoutingApplication) {

    companion object {
        private const val socialRoutingApiUrl = ""
        private const val routesUrl = ""
        private const val userRequestUrl = ""
    }

    fun requestTest(url: String, successCallback: (JSONObject) -> Unit, errorMessageHandler: (String) -> Unit) {
        val jsonObjectRequest = JsonRequestFactory(
            url,
            successCallback,
            errorMessageHandler
        )
            .addHeader("Authorization", "token ${application.token}")
            .buildJsonObjectRequest()
            .setShouldCache(false)

        application.queue.add(jsonObjectRequest)
    }

    fun requestUser(successCallback: (JSONObject) -> Unit, errorMessageHandler: (String) -> Unit) {
        val jsonObjectRequest = JsonRequestFactory(
            userRequestUrl,
            successCallback,
            errorMessageHandler
        )
            .addHeader("Authorization", "token ${application.token}")
            .buildJsonObjectRequest()
            .setShouldCache(false)

        application.queue.add(jsonObjectRequest)
    }

    fun requestRoutes(location: String, successCallback: (JSONObject) -> Unit, errorMessageHandler: (String) -> Unit) {
        val jsonArrayRequest = JsonRequestFactory(
            routesUrl,
            successCallback,
            errorMessageHandler
        )
            .addHeader("Authorization", "token ${application.token}")
            .buildJsonArrayRequest()

        application.queue.add(jsonArrayRequest)
    }


    private class JsonRequestFactory<T>(
        val url: String,
        val successCallback: (T) -> Unit,
        val errorMessageHandler: (String) -> Unit
    ) {

        var newHeaders = HashMap<String, String>()

        fun buildJsonObjectRequest(): JsonObjectRequest {
            return object : JsonObjectRequest(
                Method.GET, url, null,
                Response.Listener { response -> successCallback.invoke(response as T) },
                Response.ErrorListener { error -> defaultErrorCallback(error) }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    return newHeaders
                }
            }
        }

        fun buildJsonArrayRequest(): JsonArrayRequest {
            return object : JsonArrayRequest(
                Method.GET, url, null,
                Response.Listener { response -> successCallback.invoke(response as T) },
                Response.ErrorListener { error -> defaultErrorCallback(error) }
            ) {
                override fun getHeaders(): MutableMap<String, String> {
                    return newHeaders
                }
            }
        }

        fun addHeader(key: String, value: String): JsonRequestFactory<T> {
            newHeaders[key] = value
            return this
        }

        fun defaultErrorCallback(error: VolleyError) {
            when (error) {
                is TimeoutError -> //This indicates that the request has either time out or there is no connection
                    errorMessageHandler("The request has timed out.")
                is NoConnectionError -> errorMessageHandler("Connection not found.")
                is AuthFailureError -> //Error indicating that there was an Authentication Failure while performing the request
                    errorMessageHandler("Authentication error.")
                is ServerError -> //Indicates that the server responded with a error response
                    errorMessageHandler("Server error.")
                is NetworkError -> //Indicates that there was network error while performing the request
                    errorMessageHandler("Network error.")
                is ParseError -> // Indicates that the server response could not be parsed
                    errorMessageHandler("Error parsing the response from the server.")
            }
        }
    }

}