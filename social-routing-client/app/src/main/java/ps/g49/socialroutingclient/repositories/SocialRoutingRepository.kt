package ps.g49.socialroutingclient.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import okhttp3.internal.http.HttpCodec
import ps.g49.socialroutingclient.model.domainModel.Category
import ps.g49.socialroutingclient.model.domainModel.Point
import ps.g49.socialroutingclient.model.outputModel.RouteOutput
import ps.g49.socialroutingclient.services.webService.SocialRoutingWebService
import ps.g49.socialroutingclient.utils.Resource
import ps.g49.socialroutingclient.model.inputModel.socialRouting.*
import ps.g49.socialroutingclient.model.outputModel.AuthorizationOutput
import ps.g49.socialroutingclient.model.outputModel.RefreshAuthenticationDataOutput
import ps.g49.socialroutingclient.services.webService.interceptors.HeaderInterceptor
import retrofit2.*
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SocialRoutingRepository @Inject constructor(
    var socialRoutingWebService: SocialRoutingWebService,
    var okHttpClient: OkHttpClient,
    var objectMapper: ObjectMapper,
    @Named("socialRoutingAPIBaseUrl")
    var baseUrl: String
) : BaseRepository() {

    companion object {
        private const val UNAUTHORIZED_CODE = 401
    }

    private lateinit var authenticationData: AuthenticationDataInput

    fun getRootResource(): LiveData<Resource<SocialRoutingRootResource>> {
        val resource = MutableLiveData<Resource<SocialRoutingRootResource>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.getRootResource()
        genericEnqueue(call, resource)

        return resource
    }

    fun <T> genericGet(url: String): LiveData<Resource<T>> {
        val resource = MutableLiveData<Resource<T>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.genericGet<T>(url)
        genericEnqueue(
            call = call,
            resource = resource,
            errorHandler = ::errorHandler
        )

        return resource
    }

    fun signIn(authenticationUrl: String, idTokenString: String): LiveData<Resource<AuthenticationDataInput>> {
        val resource = MutableLiveData<Resource<AuthenticationDataInput>>()
        resource.value = Resource.loading()
        val authorizationOutput = AuthorizationOutput(idTokenString)

        val call = socialRoutingWebService.signIn(
            authenticationUrl,
            authorizationOutput
        )
        genericEnqueue(
            call = call,
            resource = resource,
            mapper = {
                val authenticationData = it.body()!!
                val userUrl = it.headers().get("Location")
                authenticationData.userUrl = userUrl
                updateAccessToken(authenticationData)
                authenticationData
            }
        )

        return resource
    }

    private fun updateAccessToken(authenticationInfo: AuthenticationDataInput) {
        authenticationData = authenticationInfo
        socialRoutingWebService = getAuthenticatedClient(authenticationData.accessToken).create(SocialRoutingWebService::class.java)
    }

    // Person Request
    fun getPerson(personUrl: String): LiveData<Resource<PersonInput>> {
        val resource = MutableLiveData<Resource<PersonInput>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.getPerson(personUrl)
        genericEnqueue(
            call = call,
            resource = resource,
            errorHandler = ::errorHandler
        )

        return resource
    }

    fun getUserRoutes(routesUrl: String): LiveData<Resource<SimplifiedRouteInputCollection>> {
        val resource = MutableLiveData<Resource<SimplifiedRouteInputCollection>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.getPersonRoutes(routesUrl)
        genericEnqueue(
            call = call,
            resource = resource,
            errorHandler = ::errorHandler
        )

        return resource
    }

    // Route Request
    fun getRoute(routeUrl: String): LiveData<Resource<RouteDetailedInput>> {
        val resource = MutableLiveData<Resource<RouteDetailedInput>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.getRoute(routeUrl)
        genericEnqueue(
            call = call,
            resource = resource,
            errorHandler = ::errorHandler
        )

        return resource
    }

    fun createRoute(routeOutput: RouteOutput): LiveData<Resource<String>> {
        val resource = MutableLiveData<Resource<String>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.createRoute(routeOutput)
        genericEnqueue(
            call = call,
            resource = resource,
            mapper = {
                it.headers().get("Location")!!
            },
            errorHandler = ::errorHandler
        )

        return resource
    }


    fun searchRoutes(
        searchRoutesUrl: String,
        locationId: String,
        categories: List<Category>,
        duration: String
    ): LiveData<Resource<SimplifiedRouteInputCollection>> {
        val resource = MutableLiveData<Resource<SimplifiedRouteInputCollection>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.searchRoutes(
            searchRoutesUrl,
            locationId,
            categories,
            duration
        )
        genericEnqueue(
            call = call,
            resource = resource,
            errorHandler = ::errorHandler
        )

        return resource
    }

    fun searchRoutes(
        searchRoutesUrl: String,
        locationId: String,
        categories: List<Category>,
        duration: String,
        coordinates: Point
    ): LiveData<Resource<SimplifiedRouteInputCollection>> {
        val resource = MutableLiveData<Resource<SimplifiedRouteInputCollection>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.searchRoutes(
            searchRoutesUrl,
            locationId,
            categories,
            duration,
            coordinates
        )
        genericEnqueue(
            call = call,
            resource = resource,
            errorHandler = ::errorHandler
        )

        return resource
    }

    fun getCategories(categoriesUrl: String): LiveData<Resource<CategoryCollectionInput>> {
        val resource = MutableLiveData<Resource<CategoryCollectionInput>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.getCategories(categoriesUrl)
        genericEnqueue(
            call = call,
            resource = resource,
            errorHandler = ::errorHandler
        )

        return resource
    }

    fun updateRoute(routeUrl: String, routeOutput: RouteOutput): LiveData<Resource<Unit>> {
        val resource = MutableLiveData<Resource<Unit>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.updateRoute(routeUrl, routeOutput)
        genericEnqueue(
            call = call,
            resource = resource,
            errorHandler = ::errorHandler
        )

        return resource
    }


    fun deleteRoute(routeUrl: String): LiveData<Resource<Unit>> {
        val resource = MutableLiveData<Resource<Unit>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.deleteRoute(routeUrl)
        genericEnqueue(
            call = call,
            resource = resource,
            errorHandler = ::errorHandler
        )

        return resource
    }

    private fun <T> errorHandler(response: Response<T>) {
        if (response.code() == UNAUTHORIZED_CODE) {
            val refreshAuthenticationDataOutput = RefreshAuthenticationDataOutput(authenticationData.refreshToken)
            val resource = MutableLiveData<Resource<Unit>>()
            resource.value = Resource.loading()

            val call = socialRoutingWebService.refreshToken(refreshAuthenticationDataOutput)
            genericEnqueue(
                call = call,
                resource = resource,
                mapper = {
                    updateAccessToken(it.body()!!)
                    it.body()
            })
        }
    }

    private fun getAuthenticatedClient(accessToken: String): Retrofit {
        val client = okHttpClient
            .newBuilder()
            .addInterceptor(HeaderInterceptor(accessToken))
            .build()

        return Retrofit
            .Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(JacksonConverterFactory.create(objectMapper))
            .build()
    }

}
