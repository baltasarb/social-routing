package ps.g49.socialroutingservice.exceptions

interface ErrorResponseBuilder {

    fun buildAndGetErrorResponse() : ProblemJsonResponse

}