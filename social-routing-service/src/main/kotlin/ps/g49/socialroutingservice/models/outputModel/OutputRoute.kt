package ps.g49.socialroutingservice.models.outputModel

data class OutputRoute(
        val name: String,
        val duration: Double,
        val location: String,
        val itinerary: String //url to points
)