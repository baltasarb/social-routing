package ps.g49.socialroutingservice.models.domainModel

data class SimplifiedRoute (
        val identifier : Int,
        val name : String,
        val rating : Double,
        val imageReference : String,
        val personIdentifier : Int
)