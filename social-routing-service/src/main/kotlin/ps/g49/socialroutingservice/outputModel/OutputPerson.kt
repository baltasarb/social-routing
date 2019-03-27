package ps.g49.socialroutingservice.outputModel

data class OutputPerson(
        val identifier: String,
        val name: String,
        val rating: Double,
        val createdRoutes: String,
        val performedRoutes: String
)