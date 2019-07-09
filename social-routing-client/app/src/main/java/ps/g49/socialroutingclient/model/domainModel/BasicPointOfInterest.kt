package ps.g49.socialroutingclient.model.domainModel

data class BasicPointOfInterest (
    val identifier: String,
    val photo: Photo?,
    val latitude: Double,
    val longitude: Double
)