package ps.g49.socialroutingservice.models.outputModel

data class ProblemJson (
        val type : String,
        val title : String,
        val status : Int,
        val detail : String,
        val instance : String
)