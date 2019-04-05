package ps.g49.socialroutingservice.model

import ps.g49.socialroutingservice.inputModel.RouteInput
import java.util.*

data class Route(
        var identifier: Long? = null, // created by db
        val location: String,
        val name: String,
        val description: String,
        var classification: Double? = null, // created by db
        var duration: Long? = null,// TODO created by service
        var dateCreated: Date? = null, // created by db
        val points: PointCollection,
        val personIdentifier: Int
) {
    /*
        Secondary constructor used for inserts on the database, where the
        fields assigned by the database are not yet present
    */
    constructor(routeInput: RouteInput) : this(
            location = routeInput.location,
            name = routeInput.name,
            description = routeInput.description,
            points = PointCollection(routeInput.points),
            personIdentifier = routeInput.personIdentifier
    )
}