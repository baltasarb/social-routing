package ps.g49.socialroutingservice.models.domainModel

data class SimplifiedRouteWithCount(
        val identifier: Int,
        val name: String,
        val rating: Double,
        val imageReference: String,
        val personIdentifier: Int,
        val count: Int
)