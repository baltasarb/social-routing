package ps.g49.socialroutingclient.model.domainModel

import android.graphics.Bitmap

data class ImageReferenceToAdapter(
    val photo: Photo,
    val funcToPhoto: (photoRef: String, maxHeight: Int, maxWidth: Int, func: (bitmap: Bitmap?) -> Unit) -> Unit
)