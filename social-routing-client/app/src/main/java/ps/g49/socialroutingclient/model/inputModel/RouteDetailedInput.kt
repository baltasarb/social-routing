package ps.g49.socialroutingclient.model.inputModel

import ps.g49.socialroutingclient.model.Point

data class RouteDetailedInput(
    val identifier: Long,
    val location: String,
    val name: String,
    val description: String,
    val rating: Double,
    val duration: Int,
    val dateCreated: String,
    val points: List<Point>,
    val categories: List<CategoryInput>,
    val ownerUrl: String
)
