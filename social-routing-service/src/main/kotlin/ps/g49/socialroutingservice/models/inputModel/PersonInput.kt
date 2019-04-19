package ps.g49.socialroutingservice.models.inputModel

data class PersonInput(
        val identifier : Int? = null,
        val name: String,
        val email: String,
        val rating : Double? = null
)