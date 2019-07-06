package ps.g49.socialroutingclient.model.inputModel.google.geocoding

data class Result (
    val address_components: List<AddressComponents>,
    val formatted_address: String,
    val geometry: Geometry,
    val place_id: String,
    val types: List<String>
)