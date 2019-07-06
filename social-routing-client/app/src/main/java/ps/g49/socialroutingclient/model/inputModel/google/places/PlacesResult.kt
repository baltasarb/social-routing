package ps.g49.socialroutingclient.model.inputModel.google.places

import com.fasterxml.jackson.annotation.JsonProperty
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.Bound
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.Geometry
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.reverse.PlusCode

data class PlacesResult(
    val geometry: Geometry,
    val viewPort: Bound?,
    val icon: String,
    val id: String,
    val name: String,
    val photos: List<PlacesPhotos>?,
    @JsonProperty("place_id")
    val placeId: String,
    @JsonProperty("plus_code")
    val plusCode: PlusCode?,
    @JsonProperty("opening_hours")
    val openingHours: PlaceOpeningHours?,
    val rating: Double?,
    val reference: String,
    val scope: String,
    val types: List<String>,
    @JsonProperty("user_rating_total")
    val userRatingTotal: Int?,
    val vicinity: String
)
