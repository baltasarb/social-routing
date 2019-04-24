package com.example.socialrouting.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.socialrouting.model.inputModel.PersonInput
import com.example.socialrouting.model.inputModel.RouteInput
import com.example.socialrouting.repositories.RouteRepository
import com.example.socialrouting.utils.Resource

class UserProfileViewModel : ViewModel() {

    private val routeRepository = RouteRepository()

    fun getUser(id: Int): LiveData<Resource<PersonInput>> = routeRepository.getPerson(id.toString())

    fun getUserRoutesFromUrl(routesUrl: String): LiveData<Resource<List<RouteInput>>> = routeRepository.getUserRoutes(routesUrl)

}