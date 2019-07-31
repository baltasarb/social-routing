package ps.g49.socialroutingclient.model.domainModel

data class Point (
    val latitude: Double,
    val longitude: Double
) {
    override fun toString(): String {
        return "$latitude,$longitude"
    }
}


