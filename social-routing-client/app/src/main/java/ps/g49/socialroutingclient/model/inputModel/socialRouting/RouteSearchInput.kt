package ps.g49.socialroutingclient.model.inputModel.socialRouting

data class RouteSearchInput (
    val identifier: Int,
    val name: String,
    val rating: Double,
    val personIdentifier: Int
)