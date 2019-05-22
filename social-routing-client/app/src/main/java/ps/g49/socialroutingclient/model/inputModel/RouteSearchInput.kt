package ps.g49.socialroutingclient.model.inputModel

data class RouteSearchInput (
    val identifier: Int,
    val name: String,
    val rating: Double,
    val personIdentifier: Int
)