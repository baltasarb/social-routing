package ps.g49.socialroutingclient.model.inputModel.socialRouting

data class SimplifiedRouteInputCollection (
    val next : String ? = null,
    val routes : List<RouteInput>
)