package ps.g49.socialroutingclient.model

import android.net.Uri

data class UserAccount (
    val name: String,
    val email: String,
    val photoUrl: Uri,
    val rating: Double = 0.0,
    var id: Int = -1,
    var accessToken: String = "",
    var refreshToken: String = ""
)