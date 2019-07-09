package ps.g49.socialroutingclient.viewModel

import android.graphics.Bitmap
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import ps.g49.socialroutingclient.model.domainModel.Point
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.GeoCodingResponse
import ps.g49.socialroutingclient.model.inputModel.google.places.PlacesResponse
import ps.g49.socialroutingclient.repositories.GoogleRepository
import ps.g49.socialroutingclient.utils.Resource
import javax.inject.Inject

class GoogleViewModel @Inject constructor(
    val googleRepository: GoogleRepository
) : ViewModel() {

    fun getGeoCoordinatesFromLocation(locationString: String): LiveData<Resource<GeoCodingResponse>> =
        googleRepository.getGeocoding(locationString)

    fun getLocationFromGeoCoordinates(location: Location): LiveData<Resource<String>> =
        googleRepository.getReverseGeocoding(location)

    fun getDirections(
        initialLocation: Point,
        destinationLocation: Point,
        modeOfTransport: String
    ): LiveData<Resource<List<Point>>> =
        googleRepository.getDirections(initialLocation, destinationLocation, modeOfTransport)

    fun findPlacesOfInterest(
        location: String,
        radius: Int
    ): LiveData<Resource<PlacesResponse>> =
        googleRepository.findPlacesOfInterest(location, radius)

    fun findPlacesOfInterestNextPage(
        pageToken: String
    ): LiveData<Resource<PlacesResponse>> =
        googleRepository.findPlacesOfInterestNextPage(pageToken)

    fun getPhotoFromReference(
        photoReference: String,
        maxHeight: Int,
        maxWidth: Int
    ): LiveData<Resource<Bitmap>> =
        googleRepository.getPhotoFromReference(photoReference, maxHeight, maxWidth)
}