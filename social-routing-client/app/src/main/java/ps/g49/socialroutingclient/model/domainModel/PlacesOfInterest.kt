package ps.g49.socialroutingclient.model.domainModel

import android.graphics.Bitmap

data class PlacesOfInterest (
    val name: String,
    val placeId: String,
    val rating: Double?,
    val openingHours: Boolean?,
    var isSaved: Boolean,
    val types: List<String>,
    val location: Point,
    val vicinity: String,
    val photo: Photo?,
    val funcToPhoto: (photoRef: String, maxHeight: Int, maxWidth: Int, func: (bitmap: Bitmap?) -> Unit) -> Unit
)