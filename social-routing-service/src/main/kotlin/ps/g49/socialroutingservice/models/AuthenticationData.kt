package ps.g49.socialroutingservice.models

class AuthenticationData(
        val creationDate : Long,
        val expirationDate: Long,
        val accessToken: String,
        val refreshToken: String,
        val personIdentifier: Int
)