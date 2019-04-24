package com.example.socialrouting.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.socialrouting.model.inputModel.PersonInput
import com.example.socialrouting.model.inputModel.RouteInput
import com.example.socialrouting.model.inputModel.RouteSearchInput
import com.example.socialrouting.model.outputModel.RouteOutput
import com.example.socialrouting.services.webService.RetrofitClient
import com.example.socialrouting.services.webService.SocialRoutingWebService
import com.example.socialrouting.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RouteRepository {

    companion object {
        const val baseUrl = "http://10.0.2.2:8080/api.sr/"
    }

    private val retrofitSocialRouting = RetrofitClient(baseUrl).getClient()
    private val socialRoutingWebService = retrofitSocialRouting.create(SocialRoutingWebService::class.java)

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

    fun getUserRoutes(routesUrl: String): LiveData<Resource<List<RouteInput>>> {
        val resource = MutableLiveData<Resource<List<RouteInput>>>()
        resource.value = Resource.loading()

        socialRoutingWebService
            .getPersonRoutes(routesUrl)
            .enqueue(object : Callback<List<RouteInput>> {

                override fun onFailure(call: Call<List<RouteInput>>, t: Throwable) {
                    resource.value = Resource.error(t.message.toString(), null)
                }

                override fun onResponse(call: Call<List<RouteInput>>, response: Response<List<RouteInput>>) {
                    val routesInput = response.body()
                    if (routesInput != null)
                        resource.value = Resource.success(routesInput)
                    else
                        resource.value = Resource.error(response.message(), null)
                }

            })

        return resource
    }

    /*
    fun createPerson(personOutput: PersonOutput): LiveData<PersonInput> {
        val liveData = MutableLiveData<PersonInput>()
        socialRoutingWebService.createPerson(personOutput).enqueue(object : Callback<PersonInput> {

            override fun onFailure(call: Call<PersonInput>, t: Throwable) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onResponse(call: Call<PersonInput>, response: Response<PersonInput>) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        })
        return liveData
    }

    fun updatePerson() {
        TODO("not implemented")
    }

    fun deletePerson() {
        TODO("not implemented")
    }

    // Route Request
    fun getRoute(routeIdentifier: String): LiveData<RouteDetailedInput> {
        val liveData = MutableLiveData<RouteDetailedInput>()
        socialRoutingWebService.getRoute(routeIdentifier).enqueue(object: Callback<RouteDetailedInput> {

            override fun onFailure(call: Call<RouteDetailedInput>, t: Throwable) {
                Log.i("SOCIAL ROUTING",t.message)
            }

            override fun onResponse(call: Call<RouteDetailedInput>, response: Response<RouteDetailedInput>) {
                Log.i("SOCIAL ROUTING",response.message())
            }

        })
        return liveData
    }
    */

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
                if (code == 200)
                    resource.value = Resource.success(url!!)
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
            .enqueue(object: Callback<List<RouteSearchInput>> {

                override fun onFailure(call: Call<List<RouteSearchInput>>, t: Throwable) {
                    resource.value = Resource.error(t.message.orEmpty(), null)
                }

                override fun onResponse(call: Call<List<RouteSearchInput>>, response: Response<List<RouteSearchInput>>) {
                    val routesInput = response.body()
                    if (routesInput != null)
                        resource.value = Resource.success(routesInput)
                    else
                        resource.value = Resource.error(response.message(), null)
                }

            })

        return resource
    }

    /*
    fun updateRoute(routeOutput: RouteOutput): LiveData<RouteInput> {
        TODO("not implemented")
    }

    fun deleteRoute(routeOutput: RouteOutput):LiveData<RouteInput> {
        TODO("not implemented")
    }*/

}