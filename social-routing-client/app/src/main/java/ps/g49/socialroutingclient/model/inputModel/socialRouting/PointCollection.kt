package ps.g49.socialroutingclient.model.inputModel.socialRouting

import ps.g49.socialroutingclient.model.Point

data class PointCollection(
    val points: List<Point>
) {
    /*
    used to insert json into the database
     */
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[")
        points.forEach { point ->
            val jsonPoint = buildAndGetJsonPoint(point.latitude, point.longitude)
            sb.append(jsonPoint)
        }
        sb.append("]")
        return sb.toString()
    }

    private fun buildAndGetJsonPoint(latitude: Double, longitude: Double): String {
        return "{\"latitude\":$latitude, \"longitude\":$longitude}"
    }
}