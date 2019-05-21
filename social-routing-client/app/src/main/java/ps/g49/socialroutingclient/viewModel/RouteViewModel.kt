package ps.g49.socialroutingclient.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ps.g49.socialroutingclient.model.outputModel.RouteOutput
import ps.g49.socialroutingclient.repositories.RouteRepository
import ps.g49.socialroutingclient.utils.Resource
import ps.g49.socialroutingclient.model.inputModel.CategoryCollectionInput
import ps.g49.socialroutingclient.model.inputModel.RouteDetailedInput
import ps.g49.socialroutingclient.model.inputModel.RouteSearchInput

class RouteViewModel: ViewModel() {

    private val routeRepository = RouteRepository()

    fun createRoute(routeOutput: RouteOutput): LiveData<Resource<String>> =
            routeRepository.createRoute(routeOutput)

    fun searchRoutes(): LiveData<Resource<List<RouteSearchInput>>> =
            routeRepository.searchRoutes()

    fun getRoute(routeId: Int): LiveData<Resource<RouteDetailedInput>>
            = routeRepository.getRoute(routeId)

    fun getRouteCategories(): LiveData<Resource<CategoryCollectionInput>> =
            routeRepository.getCategories()

}