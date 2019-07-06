package ps.g49.socialroutingclient.model.inputModel.google.geocoding.reverse

import com.fasterxml.jackson.annotation.JsonProperty
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.Bound
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.PointGeocoding

data class ReverseGeometry (
    val bounds: Bound?,
    val location: PointGeocoding,
    @JsonProperty("location_type")
    val locationType: String,
    val viewport: Bound
)
