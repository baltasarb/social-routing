package ps.g49.socialroutingclient.webService

import ps.g49.socialroutingclient.model.outputModel.PersonOutput
import ps.g49.socialroutingclient.model.outputModel.RouteOutput
import ps.g49.socialroutingclient.model.inputModel.*
import ps.g49.socialroutingclient.model.outputModel.AuthorizationOutput
import retrofit2.Call
import retrofit2.http.*

interface SocialRoutingWebService {

    @POST("authentication/google")
    fun signIn(@Body authorizationOutput: AuthorizationOutput): Call<AuthenticationDataInput>

    // Person Requests
    @GET("persons/{personIdentifier}")
    fun getPerson(@Path("personIdentifier") personIdentifier: String): Call<PersonInput>

    @POST("persons")
    fun createPerson(@Body personOutput: PersonOutput): Call<PersonInput>

    @PUT("persons/{personIdentifier}")
    fun updatePerson(@Path("personIdentifier") personIdentifier: String, @Body person: PersonInput): Call<PersonInput>

    @DELETE("persons/{personIdentifier}")
    fun deletePerson(@Path("personIdentifier") personIdentifier: String, @Body peron: PersonInput): Call<PersonInput>

    @GET
    fun getPersonRoutes(@Url routesUrl: String): Call<SimplifiedRouteInputCollection>

    //  Route Requests
    @GET("routes/{routeIdentifier}")
    fun getRoute(@Path("routeIdentifier") routeIdentifier: Int): Call<RouteDetailedInput>

    @POST("routes")
    fun createRoute(@Body routeOutput: RouteOutput): Call<Void>

    @PUT("routes")
    fun updateRoute(@Body routeOutput: RouteOutput): Call<RouteInput>

    @DELETE("routes/{routeIdentifier}")
    fun deleteRoute(@Path("routeIdentifier") routeIdentifier: Int): Call<Void>

    @GET("routes")
    fun searchRoutes(): Call<List<RouteSearchInput>>

    @GET("categories")
    fun getCategories(): Call<CategoryCollectionInput>
}