package ps.g49.socialroutingservice.models.requests

data class PersonRoutesRequest (
        val identifier : Int,
        val page : Int
){
    companion object{
        fun build(identifier: Int, params: HashMap<String, String>): PersonRoutesRequest {
            val page = params["page"]?.toInt()
            return PersonRoutesRequest(
                    identifier = identifier,
                    page = page?:1
            )
        }
    }
}



