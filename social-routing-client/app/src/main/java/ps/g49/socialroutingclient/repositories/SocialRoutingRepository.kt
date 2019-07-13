package ps.g49.socialroutingclient.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import ps.g49.socialroutingclient.model.domainModel.Category
import ps.g49.socialroutingclient.model.domainModel.Point
import ps.g49.socialroutingclient.model.outputModel.RouteOutput
import ps.g49.socialroutingclient.services.webService.SocialRoutingWebService
import ps.g49.socialroutingclient.utils.Resource
import ps.g49.socialroutingclient.model.inputModel.socialRouting.*
import ps.g49.socialroutingclient.model.outputModel.AuthorizationOutput
import ps.g49.socialroutingclient.services.webService.interceptors.HeaderInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
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
        genericEnqueue(call, resource)

        return resource
    }

    fun signIn(authenticationUrl: String, idTokenString: String): LiveData<Resource<AuthenticationDataInput>> {
        val resource = MutableLiveData<Resource<AuthenticationDataInput>>()
        resource.value = Resource.loading()
        val authorizationOutput = AuthorizationOutput(idTokenString)

        socialRoutingWebService
            .signIn(
                authenticationUrl,
                authorizationOutput
            )
            .enqueue(object : Callback<AuthenticationDataInput> {
                override fun onFailure(call: Call<AuthenticationDataInput>, t: Throwable) {
                    resource.value = Resource.error(t.message.toString(), null)
                }

                override fun onResponse(
                    call: Call<AuthenticationDataInput>,
                    response: Response<AuthenticationDataInput>
                ) {
                    val code = response.code()
                    if (code == 200) {
                        val authenticationData = response.body()!!
                        val userUrl = response.headers().get("Location")
                        authenticationData.userUrl = userUrl
                        resource.value = Resource.success(authenticationData)
                        updateAccessToken(authenticationData.accessToken)
                    } else
                        resource.value = Resource.error(response.message(), null)
                }
            })

        return resource
    }

    private fun updateAccessToken(accessToken: String) {
        socialRoutingWebService = getAuthenticatedClient(accessToken).create(SocialRoutingWebService::class.java)
    }

    // Person Request
    fun getPerson(personUrl: String): LiveData<Resource<PersonInput>> {
        val resource = MutableLiveData<Resource<PersonInput>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.getPerson(personUrl)
        genericEnqueue(call, resource)

        return resource
    }

    fun getUserRoutes(routesUrl: String): LiveData<Resource<SimplifiedRouteInputCollection>> {
        val resource = MutableLiveData<Resource<SimplifiedRouteInputCollection>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.getPersonRoutes(routesUrl)
        genericEnqueue(call, resource)

        return resource
    }

    // Route Request
    fun getRoute(routeUrl: String): LiveData<Resource<RouteDetailedInput>> {
        val resource = MutableLiveData<Resource<RouteDetailedInput>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.getRoute(routeUrl)
        genericEnqueue(call, resource)

        return resource
    }

    fun createRoute(routeOutput: RouteOutput): LiveData<Resource<String>> {
        val resource = MutableLiveData<Resource<String>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.createRoute(routeOutput)
        genericEnqueue(call, resource) {
            it.headers().get("Location")!!
        }

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
        genericEnqueue(call, resource)

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
        genericEnqueue(call, resource)

        return resource
    }

    fun getCategories(categoriesUrl: String): LiveData<Resource<CategoryCollectionInput>> {
        val resource = MutableLiveData<Resource<CategoryCollectionInput>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.getCategories(categoriesUrl)
        genericEnqueue(call, resource)

        return resource
    }

    fun updateRoute(routeUrl: String, routeOutput: RouteOutput): LiveData<Resource<Unit>> {
        val resource = MutableLiveData<Resource<Unit>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.updateRoute(routeUrl, routeOutput)
        genericEnqueue(call, resource)

        return resource
    }


    fun deleteRoute(routeUrl: String): LiveData<Resource<Unit>> {
        val resource = MutableLiveData<Resource<Unit>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.deleteRoute(routeUrl)
        genericEnqueue(call, resource)

        return resource
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
