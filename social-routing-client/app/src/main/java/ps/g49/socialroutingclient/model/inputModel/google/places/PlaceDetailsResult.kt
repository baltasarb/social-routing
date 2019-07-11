package ps.g49.socialroutingclient.model.inputModel.google.places

import ps.g49.socialroutingclient.model.inputModel.google.geocoding.Geometry

data class PlaceDetailsResult(
    val geometry: Geometry,
    val name: String
)
