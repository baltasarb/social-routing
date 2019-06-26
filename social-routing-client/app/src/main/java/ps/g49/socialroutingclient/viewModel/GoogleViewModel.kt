package ps.g49.socialroutingclient.viewModel

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

    companion object {
        const val googleMapsKey = "AIzaSyCpwLrcZPuDfuuDBRDKasrPAzviHiyc4N8"
    }

    fun getGeoCoordinatesFromLocation(address: String): LiveData<Resource<PointGeocoding>> =
        googleRepository.getGeoCoordinatesFromLocation(address, googleMapsKey)

    fun getDirections(
        initialLocation: Point,
        destinationLocation: Point,
        modeOfTransport: String
    ): LiveData<Resource<List<Point>>> =
        googleRepository.getDirections(initialLocation, destinationLocation, modeOfTransport, googleMapsKey)


}