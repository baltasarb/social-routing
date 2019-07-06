package ps.g49.socialroutingclient.model.domainModel

import android.net.Uri

data class UserAccount (
    val name: String,
    val email: String,
    val photoUrl: Uri,
    val rating: Double = 0.0,
    var userUrl: String = "",
    var accessToken: String = "",
    var refreshToken: String = ""
)