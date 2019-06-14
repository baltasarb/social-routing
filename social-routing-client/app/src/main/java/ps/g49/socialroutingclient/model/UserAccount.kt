package ps.g49.socialroutingclient.model

import android.net.Uri

data class UserAccount (
    val name: String,
    val email: String,
    val photoUrl: Uri
)