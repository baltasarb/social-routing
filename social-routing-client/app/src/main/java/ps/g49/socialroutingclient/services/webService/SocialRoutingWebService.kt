package ps.g49.socialroutingclient.services.webService

import ps.g49.socialroutingclient.model.domainModel.Category
import ps.g49.socialroutingclient.model.domainModel.Point
import ps.g49.socialroutingclient.model.outputModel.PersonOutput
import ps.g49.socialroutingclient.model.outputModel.RouteOutput
import ps.g49.socialroutingclient.model.inputModel.socialRouting.*
import ps.g49.socialroutingclient.model.outputModel.AuthorizationOutput
import retrofit2.Call
import retrofit2.http.*

interface SocialRoutingWebService {

    // GET Methods
    @GET(".")
    fun getRootResource(): Call<SocialRoutingRootResource>

    @GET
    fun searchRoutes(
        @Url searchRoutesUrl: String,
        @Query("location") location: String,
        @Query("categories") categories: List<Category>,
        @Query("duration") duration: String
    ): Call<SimplifiedRouteInputCollection>

    @GET
    fun searchRoutes(
        @Url searchRoutesUrl: String,
        @Query("location") location: String,
        @Query("categories") categories: List<Category>,
        @Query("duration") duration: String,
        @Query("coordinates") coordinates: Point
    ): Call<SimplifiedRouteInputCollection>

    @GET
    fun getCategories(@Url categoriesUrl: String): Call<CategoryCollectionInput>

    @GET
    fun getPerson(@Url personUrl: String): Call<PersonInput>

    @GET
    fun getPersonRoutes(@Url routesUrl: String): Call<SimplifiedRouteInputCollection>

    @GET
    fun getRoute(@Url routeUrl: String): Call<RouteDetailedInput>


    // POST methods
    @POST
    fun signIn(
        @Url authenticationUrl: String,
        @Body authorizationOutput: AuthorizationOutput
    ): Call<AuthenticationDataInput>

    @POST("routes")
    fun createRoute(@Body routeOutput: RouteOutput): Call<Unit>

    // PUT methods
    @PUT
    @POST("routes/{identifier}")
    fun updateRoute(
        @Url routeUrl: String,
        @Body routeOutput: RouteOutput
    ): Call<Unit>

    //DELETE methods
    @DELETE
    fun deleteRoute(
        @Url routeUrl: String
    ): Call<Unit>

    @GET
    fun <T> genericGet(
        @Url url: String
    ): Call<T>

}