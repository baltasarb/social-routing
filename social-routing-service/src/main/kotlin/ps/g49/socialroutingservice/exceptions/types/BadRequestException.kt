package ps.g49.socialroutingservice.exceptions.types

class BadRequestException(
        val type: String = "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#bad-request",
        val status: Int = 400,
        val title: String = "The request is poorly formed."
) : Exception()