package ps.g49.socialroutingservice.models.outputModel

data class PersonOutput(
        val identifier: Int,
        val name: String,
        val email : String,
        val rating: Double,
        val routesUrl : String
)