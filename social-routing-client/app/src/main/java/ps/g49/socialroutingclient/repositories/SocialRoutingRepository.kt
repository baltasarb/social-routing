package ps.g49.socialroutingclient.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ps.g49.socialroutingclient.model.outputModel.RouteOutput
import ps.g49.socialroutingclient.webService.RetrofitClient
import ps.g49.socialroutingclient.webService.SocialRoutingWebService
import ps.g49.socialroutingclient.utils.Resource
import ps.g49.socialroutingclient.model.inputModel.socialRouting.*
import ps.g49.socialroutingclient.model.outputModel.AuthorizationOutput
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialRoutingRepository @Inject constructor(
    var socialRoutingWebService: SocialRoutingWebService
) : BaseRepository() {

    private fun updateAccessToken(accessToken: String) {
        socialRoutingWebService = RetrofitClient("http://10.0.2.2:8080/api.sr/")
            .getAuthenticatedClient(accessToken)
            .create(SocialRoutingWebService::class.java)
    }

    fun getRootResource(): LiveData<Resource<SocialRoutingRootResource>> {
        val resource = MutableLiveData<Resource<SocialRoutingRootResource>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.getRootResource()
        genericEnqueue(call, resource)

        return resource
    }

    fun signIn(authenticationUrl: String, idTokenString: String): LiveData<Resource<AuthenticationDataInput>> {
        val resource = MutableLiveData<Resource<AuthenticationDataInput>>()
        resource.value = Resource.loading()

        socialRoutingWebService
            .signIn(
                authenticationUrl,
                AuthorizationOutput(idTokenString)
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

        socialRoutingWebService
            .createRoute(routeOutput)
            .enqueue(object : Callback<Void> {

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    resource.value = Resource.error(t.message.toString(), null)
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    val code = response.code()
                    val url = response.headers().get("Location")
                    if (code == 200) {
                        val list = url!!.split("/")
                        val id = list[list.size - 1]
                        resource.value = Resource.success(id)
                    } else
                        resource.value = Resource.error(response.message(), null)
                }
            })

        return resource
    }

    fun searchRoutes(searchRoutesUrl: String, location: String): LiveData<Resource<SimplifiedRouteInputCollection>> {
        val resource = MutableLiveData<Resource<SimplifiedRouteInputCollection>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.searchRoutes(searchRoutesUrl, location)
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


    fun updateRoute(routeOutput: RouteOutput): LiveData<RouteInput> {
        TODO("not implemented")
    }

    fun deleteRoute(routeUrl: String): LiveData<Resource<Void>> {
        val resource = MutableLiveData<Resource<Void>>()
        resource.value = Resource.loading()

        val call = socialRoutingWebService.deleteRoute(routeUrl)
        genericEnqueue(call, resource)

        return resource
    }

}