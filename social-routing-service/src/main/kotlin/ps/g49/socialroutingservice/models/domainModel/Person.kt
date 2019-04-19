package ps.g49.socialroutingservice.models.domainModel

data class Person(
        val identifier : Int? = null,
        val name: String,
        val email : String,
        val rating : Double? = null
)