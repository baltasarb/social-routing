package ps.g49.socialroutingclient.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ps.g49.socialroutingclient.model.outputModel.RouteOutput
import ps.g49.socialroutingclient.webService.RetrofitClient
import ps.g49.socialroutingclient.webService.SocialRoutingWebService
import ps.g49.socialroutingclient.utils.Resource
import ps.g49.socialroutingclient.model.inputModel.*
import ps.g49.socialroutingclient.model.outputModel.AuthorizationOutput
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SocialRoutingRepository @Inject constructor(
    var socialRoutingWebService: SocialRoutingWebService
){

    private fun updateAccessToken(accessToken: String) {
        socialRoutingWebService = RetrofitClient("http://10.0.2.2:8080/api.sr/")
            .getAuthenticatedClient(accessToken)
            .create(SocialRoutingWebService::class.java)
    }

    fun signIn(idTokenString: String): LiveData<Resource<AuthenticationDataInput>> {
        val resource = MutableLiveData<Resource<AuthenticationDataInput>>()
        resource.value = Resource.loading()

        socialRoutingWebService
            .signIn(AuthorizationOutput(idTokenString))
            .enqueue(object : Callback<AuthenticationDataInput> {
                override fun onFailure(call: Call<AuthenticationDataInput>, t: Throwable) {
                    resource.value = Resource.error(t.message.toString(), null)
                }

                override fun onResponse(call: Call<AuthenticationDataInput>, response: Response<AuthenticationDataInput>) {
                    val code = response.code()
                    if (code == 200) {
                        val authenticationData = response.body()!!
                        resource.value = Resource.success(authenticationData)
                        updateAccessToken(authenticationData.accessToken)
                    }
                    else
                        resource.value = Resource.error(response.message(), null)
                }
            })

        return resource
    }

    // Person Request
    fun getPerson(personIdentifier: String): LiveData<Resource<PersonInput>> {
        val resource = MutableLiveData<Resource<PersonInput>>()
        resource.value = Resource.loading()

        socialRoutingWebService
            .getPerson(personIdentifier)
            .enqueue(object : Callback<PersonInput> {

                override fun onFailure(call: Call<PersonInput>, t: Throwable) {
                    resource.value = Resource.error(t.message.toString(), null)
                }

                override fun onResponse(call: Call<PersonInput>, response: Response<PersonInput>) {
                    val personInput = response.body()
                    if (personInput != null)
                        resource.value = Resource.success(personInput)
                    else
                        resource.value = Resource.error(response.message(), null)
                }

            })

        return resource
    }

    fun getUserRoutes(routesUrl: String): LiveData<Resource<SimplifiedRouteInputCollection>> {
        val resource = MutableLiveData<Resource<SimplifiedRouteInputCollection>>()
        resource.value = Resource.loading()

        socialRoutingWebService
            .getPersonRoutes(routesUrl)
            .enqueue(object : Callback<SimplifiedRouteInputCollection> {

                override fun onFailure(call: Call<SimplifiedRouteInputCollection>, t: Throwable) {
                    resource.value = Resource.error(t.message.toString(), null)
                }

                override fun onResponse(call: Call<SimplifiedRouteInputCollection>, response: Response<SimplifiedRouteInputCollection>) {
                    val routesInput = response.body()
                    if (routesInput != null)
                        resource.value = Resource.success(routesInput)
                    else
                        resource.value = Resource.error(response.message(), null)
                }

            })

        return resource
    }

    // Route Request
    fun getRoute(routeIdentifier: Int): LiveData<Resource<RouteDetailedInput>> {
        val resource = MutableLiveData<Resource<RouteDetailedInput>>()
        resource.value = Resource.loading()

        socialRoutingWebService
            .getRoute(routeIdentifier)
            .enqueue(object : Callback<RouteDetailedInput> {

                override fun onFailure(call: Call<RouteDetailedInput>, t: Throwable) {
                    resource.value = Resource.error(t.message.toString(), null)
                }

                override fun onResponse(call: Call<RouteDetailedInput>, response: Response<RouteDetailedInput>) {
                    val code = response.code()
                    if (code == 200)
                        resource.value = Resource.success(response.body()!!)
                    else
                        resource.value = Resource.error(response.message(), null)
                }

            })

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
                    }
                    else
                        resource.value = Resource.error(response.message(), null)
                }
            })

        return resource
    }

    fun searchRoutes(): LiveData<Resource<List<RouteSearchInput>>> {
        val resource = MutableLiveData<Resource<List<RouteSearchInput>>>()
        resource.value = Resource.loading()

        socialRoutingWebService
            .searchRoutes()
            .enqueue(object : Callback<List<RouteSearchInput>> {

                override fun onFailure(call: Call<List<RouteSearchInput>>, t: Throwable) {
                    resource.value = Resource.error(t.message.orEmpty(), null)
                }

                override fun onResponse(
                    call: Call<List<RouteSearchInput>>,
                    response: Response<List<RouteSearchInput>>
                ) {
                    val routesInput = response.body()
                    if (routesInput != null)
                        resource.value = Resource.success(routesInput)
                    else
                        resource.value = Resource.error(response.message(), null)
                }

            })

        return resource
    }

    fun getCategories(): LiveData<Resource<CategoryCollectionInput>> {
        val resource = MutableLiveData<Resource<CategoryCollectionInput>>()
        resource.value = Resource.loading()

        socialRoutingWebService
            .getCategories()
            .enqueue(object : Callback<CategoryCollectionInput> {

                override fun onFailure(call: Call<CategoryCollectionInput>, t: Throwable) {
                    resource.value = Resource.error(t.message.orEmpty(), null)
                }

                override fun onResponse(
                    call: Call<CategoryCollectionInput>,
                    response: Response<CategoryCollectionInput>
                ) {
                    val categoryCollectionInput = response.body()
                    if (categoryCollectionInput != null)
                        resource.value = Resource.success(categoryCollectionInput)
                    else
                        resource.value = Resource.error(response.message(), null)
                }

            })

        return resource
    }


    fun updateRoute(routeOutput: RouteOutput): LiveData<RouteInput> {
        TODO("not implemented")
    }

    fun deleteRoute (routeIdentifier: Int) : LiveData<Resource<Void>> {
        val resource = MutableLiveData<Resource<Void>>()
        resource.value = Resource.loading()

        socialRoutingWebService
            .deleteRoute(routeIdentifier)
            .enqueue(object : Callback<Void> {

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    resource.value = Resource.error(t.message.orEmpty(), null)
                }

                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    val code = response.code()
                    if (code == 200)
                        resource.value = Resource.success()
                    else
                        resource.value = Resource.error(response.message(), null)
                }

            })

        return resource
    }

}