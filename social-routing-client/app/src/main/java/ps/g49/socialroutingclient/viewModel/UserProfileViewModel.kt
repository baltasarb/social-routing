package ps.g49.socialroutingclient.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ps.g49.socialroutingclient.model.inputModel.PersonInput
import ps.g49.socialroutingclient.model.inputModel.RouteInput
import ps.g49.socialroutingclient.repositories.RouteRepository
import ps.g49.socialroutingclient.utils.Resource

class UserProfileViewModel : ViewModel() {

    private val routeRepository = RouteRepository()

    fun getUser(id: Int): LiveData<Resource<PersonInput>> = routeRepository.getPerson(id.toString())

    fun getUserRoutesFromUrl(routesUrl: String): LiveData<Resource<List<RouteInput>>> = routeRepository.getUserRoutes(routesUrl)

    fun deleteUserRoute(routeId: Int): LiveData<Resource<Void>> = routeRepository.deleteRoute(routeId)
}