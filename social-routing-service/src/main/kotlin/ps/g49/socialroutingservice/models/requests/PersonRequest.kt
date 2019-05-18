package ps.g49.socialroutingservice.models.requests

data class PersonRequest(
        var identifier: Int? = null,
        val name: String,
        val email: String,
        val rating: Double? = null
)