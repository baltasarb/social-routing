package ps.g49.socialroutingservice.models.requests

data class PersonRoutesRequest(
        val identifier: Int,
        val page: Int
) {
    companion object {
        fun build(identifier: Int, page: Int): PersonRoutesRequest {
            return PersonRoutesRequest(
                    identifier = identifier,
                    page = page
            )
        }
    }
}



