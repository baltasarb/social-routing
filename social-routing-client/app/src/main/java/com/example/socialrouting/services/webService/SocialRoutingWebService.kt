package com.example.socialrouting.services.webService

import com.example.socialrouting.model.inputModel.*
import com.example.socialrouting.model.outputModel.PersonOutput
import com.example.socialrouting.model.outputModel.RouteOutput
import retrofit2.Call
import retrofit2.http.*

interface SocialRoutingWebService {

    companion object {
        const val HEADER_CONTENT_TYPE = "Content-type: application/json"
    }

    // Person Requests
    @GET("persons/{personIdentifier}")
    fun getPerson(@Path("personIdentifier") personIdentifier: String): Call<PersonInput>

    @Headers(HEADER_CONTENT_TYPE)
    @POST("persons")
    fun createPerson(@Body personOutput: PersonOutput): Call<PersonInput>

    @PUT("persons/{personIdentifier}")
    fun updatePerson(@Path("personIdentifier") personIdentifier: String, @Body person: PersonInput): Call<PersonInput>

    @DELETE("persons/{personIdentifier}")
    fun deletePerson(@Path("personIdentifier") personIdentifier: String, @Body peron: PersonInput): Call<PersonInput>

    @GET
    fun getPersonRoutes(@Url routesUrl: String): Call<List<RouteInput>>

    //  Route Requests
    @GET("routes/{routeIdentifier}")
    fun getRoute(@Path("routeIdentifier") routeIdentifier: Int): Call<RouteDetailedInput>

    @Headers(HEADER_CONTENT_TYPE)
    @POST("routes")
    fun createRoute(@Body routeOutput: RouteOutput): Call<Void>

    @PUT("routes")
    fun updateRoute(@Body routeOutput: RouteOutput): Call<RouteInput>

    @DELETE("routes")
    fun deleteRoute(@Body routeOutput: RouteOutput): Call<RouteInput>

    @GET("routes")
    fun searchRoutes(): Call<List<RouteSearchInput>>

    @GET("categories")
    fun getCategories(): Call<CategoryCollectionInput>
}