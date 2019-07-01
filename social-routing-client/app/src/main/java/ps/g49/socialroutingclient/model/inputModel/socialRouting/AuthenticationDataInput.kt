package ps.g49.socialroutingclient.model.inputModel.socialRouting

data class AuthenticationDataInput (
    val accessToken: String,
    val refreshToken: String,
    var userUrl: String?
)