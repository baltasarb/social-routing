package com.example.socialrouting.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.socialrouting.model.inputModel.RouteDetailedInput
import com.example.socialrouting.model.inputModel.RouteInput
import com.example.socialrouting.model.inputModel.RouteSearchInput
import com.example.socialrouting.model.outputModel.RouteOutput
import com.example.socialrouting.repositories.RouteRepository
import com.example.socialrouting.utils.Resource

class RouteViewModel: ViewModel() {

    private val routeRepository = RouteRepository()

    fun createRoute(routeOutput: RouteOutput): LiveData<Resource<String>> =
            routeRepository.createRoute(routeOutput)

    // ToDo upgrade search algorithm
    fun searchRoutes(): LiveData<Resource<List<RouteSearchInput>>> =
            routeRepository.searchRoutes()

    fun getRoute(routeId: Int): LiveData<Resource<RouteDetailedInput>>
     = routeRepository.getRoute(routeId)

}