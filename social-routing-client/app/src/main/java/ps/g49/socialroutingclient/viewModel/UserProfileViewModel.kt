package ps.g49.socialroutingclient.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ps.g49.socialroutingclient.model.inputModel.socialRouting.PersonInput
import ps.g49.socialroutingclient.model.inputModel.socialRouting.SimplifiedRouteInputCollection
import ps.g49.socialroutingclient.repositories.SocialRoutingRepository
import ps.g49.socialroutingclient.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserProfileViewModel @Inject constructor(
    val routeRepository: SocialRoutingRepository
) : ViewModel() {

    fun getUser(personUrl: String): LiveData<Resource<PersonInput>> =
        routeRepository.getPerson(personUrl)

    fun getUserRoutesFromUrl(routesUrl: String): LiveData<Resource<SimplifiedRouteInputCollection>> =
        routeRepository.getUserRoutes(routesUrl)

    fun deleteUserRoute(routesUrl: String): LiveData<Resource<Unit>> =
        routeRepository.deleteRoute(routesUrl)

    fun <T> genericGet(url: String): LiveData<Resource<T>> =
        routeRepository.genericGet(url)
}