package ps.g49.socialroutingservice.models.requests

data class PersonRequest(
        var identifier: Int? = null,
        val rating: Double? = null
)