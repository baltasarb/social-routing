package ps.g49.socialroutingclient.repositories

import androidx.lifecycle.MutableLiveData
import ps.g49.socialroutingclient.utils.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

abstract class BaseRepository {

    fun <T, R> genericEnqueue(call: Call<T>, resource: MutableLiveData<Resource<R>>, mapper: (response: Response<T>) -> R) {
        call.enqueue(object : Callback<T> {

            override fun onFailure(call: Call<T>, t: Throwable) {
                resource.value = Resource.error(t.message.toString(), null)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    resource.value = Resource.success(mapper(response))
                }
                else {
                    resource.value = Resource.error(response.message(), null)
                }
            }

        })
    }

    fun <T> genericEnqueue(call: Call<T>, resource: MutableLiveData<Resource<T>>) {
        call.enqueue(object : Callback<T> {

            override fun onFailure(call: Call<T>, t: Throwable) {
                resource.value = Resource.error(t.message.toString(), null)
            }

            override fun onResponse(call: Call<T>, response: Response<T>) {
                if (response.isSuccessful) {
                    val value = response.body()!!
                    resource.value = Resource.success(value)
                }
                else {
                    resource.value = Resource.error(response.message(), null)
                }
            }

        })
    }

}