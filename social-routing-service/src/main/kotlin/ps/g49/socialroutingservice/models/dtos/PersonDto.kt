package ps.g49.socialroutingservice.models.dtos

data class PersonDto(
        var identifier: Int? = null,
        val name: String,
        val email: String,
        val rating: Double? = null
)