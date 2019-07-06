package ps.g49.socialroutingclient.model.outputModel

import ps.g49.socialroutingclient.model.domainModel.Point

data class RouteOutput (
    val locationId : String,
    val name : String,
    val description : String,
    val personIdentifier: Int,
    val points : List<Point>, // json in the db
    val categories : List<CategoryOutput>
)