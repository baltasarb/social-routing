package ps.g49.socialroutingservice.models.requests

data class DeleteRouteRequest(
        val identifier: Int,
        val personIdentifier: Int
) {
    companion object {
        fun build(identifier: Int, personIdentifier: Int): DeleteRouteRequest {
            return DeleteRouteRequest(identifier, personIdentifier)
        }
    }
}