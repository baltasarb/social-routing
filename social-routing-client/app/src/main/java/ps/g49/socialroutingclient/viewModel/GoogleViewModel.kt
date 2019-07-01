package ps.g49.socialroutingclient.viewModel

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ps.g49.socialroutingclient.model.Point
import ps.g49.socialroutingclient.model.inputModel.geocoding.PointGeocoding
import ps.g49.socialroutingclient.repositories.GoogleRepository
import ps.g49.socialroutingclient.utils.Resource
import javax.inject.Inject

class GoogleViewModel @Inject constructor(
    val googleRepository: GoogleRepository
) : ViewModel() {

    fun getGeoCoordinatesFromLocation(address: String): LiveData<Resource<PointGeocoding>> =
        googleRepository.getGeocoding(address)

    fun getLocationFromGeoCoordinates(location: Location): LiveData<Resource<String>> =
        googleRepository.getReverseGeocoding(location)

    fun getDirections(
        initialLocation: Point,
        destinationLocation: Point,
        modeOfTransport: String
    ): LiveData<Resource<List<Point>>> =
        googleRepository.getDirections(initialLocation, destinationLocation, modeOfTransport)

}