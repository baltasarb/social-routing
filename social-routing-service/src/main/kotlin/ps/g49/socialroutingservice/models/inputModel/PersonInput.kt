package ps.g49.socialroutingservice.models.inputModel

data class PersonInput(
        val name: String,
        val email: String,
        val rating : Double? = null
)