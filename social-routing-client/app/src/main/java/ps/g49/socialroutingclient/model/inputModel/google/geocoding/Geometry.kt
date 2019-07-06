package ps.g49.socialroutingclient.model.inputModel.google.geocoding

data class Geometry (
    val location: PointGeocoding,
    val bounds: Bound?,
    val location_type: String?,
    val viewport: Bound
)
