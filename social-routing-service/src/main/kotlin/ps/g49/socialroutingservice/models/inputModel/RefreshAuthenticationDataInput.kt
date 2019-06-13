package ps.g49.socialroutingservice.models.inputModel

data class RefreshAuthenticationDataInput(
        val accessToken: String,
        val refreshToken: String,
        val personIdentifier: Int
)