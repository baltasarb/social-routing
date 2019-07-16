package ps.g49.socialroutingservice.models.domainModel

data class SimplifiedRouteCollection(
        val routes: List<SimplifiedRoute>,
        val nextPage: Int? = null
)