package ps.g49.socialroutingclient.model.inputModel.google.geocoding.reverse

import com.fasterxml.jackson.annotation.JsonProperty
import ps.g49.socialroutingclient.model.inputModel.google.geocoding.AddressComponents

data class ReverseResult (
    @JsonProperty("address_components")
    val addressComponents: List<AddressComponents>,
    @JsonProperty("formatted_address")
    val formattedAddress: String,
    val geometry: ReverseGeometry,
    @JsonProperty("place_id")
    val placeId: String,
    @JsonProperty("plus_code")
    val plusCode: PlusCode?,
    val types: List<String>
)