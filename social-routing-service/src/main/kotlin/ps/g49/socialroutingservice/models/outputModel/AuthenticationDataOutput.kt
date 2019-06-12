package ps.g49.socialroutingservice.models.outputModel

class AuthenticationDataOutput(
        val creationDate : Long,
        val expirationDate: Long,
        val accessToken: String,
        val refreshToken: String
)