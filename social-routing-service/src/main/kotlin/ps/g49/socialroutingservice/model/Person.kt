package ps.g49.socialroutingservice.model

data class Person(
        val firstName: String,
        val lastName: String,
        val email: String? = null
)