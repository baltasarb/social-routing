package ps.g49.socialroutingservice.exceptions

data class ProblemJsonResponse (
        val type : String,
        val title : String,
        val status : Int,
        val detail : String,
        val instance : String
)