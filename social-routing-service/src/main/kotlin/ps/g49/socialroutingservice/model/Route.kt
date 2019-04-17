package ps.g49.socialroutingservice.model

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
)