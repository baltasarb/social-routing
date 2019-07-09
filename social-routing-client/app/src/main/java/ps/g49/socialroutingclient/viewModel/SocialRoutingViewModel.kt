package ps.g49.socialroutingclient.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ps.g49.socialroutingclient.model.inputModel.socialRouting.*
import ps.g49.socialroutingclient.model.outputModel.RouteOutput
import ps.g49.socialroutingclient.repositories.SocialRoutingRepository
import ps.g49.socialroutingclient.utils.Resource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocialRoutingViewModel @Inject constructor (
    val routeRepository : SocialRoutingRepository
): ViewModel() {

    val root = getRootResource()
    //val search = searchRoutes(root.value.data.routeSearchUrl)

    fun getRootResource(): LiveData<Resource<SocialRoutingRootResource>> =
            routeRepository.getRootResource()

    fun createRoute(routeOutput: RouteOutput): LiveData<Resource<String>> =
            routeRepository.createRoute(routeOutput)

    fun searchRoutes(searchRoutesUrl: String, location: String): LiveData<Resource<SimplifiedRouteInputCollection>> =
            routeRepository.searchRoutes(searchRoutesUrl, location)

    fun getRoute(routeUrl: String): LiveData<Resource<RouteDetailedInput>>
            = routeRepository.getRoute(routeUrl)

    fun getRouteCategories(categoriesUrl: String): LiveData<Resource<CategoryCollectionInput>> =
            routeRepository.getCategories(categoriesUrl)

    fun signIn(authenticationUrl: String, idTokenString: String): LiveData<Resource<AuthenticationDataInput>> =
            routeRepository.signIn(authenticationUrl, idTokenString)

}