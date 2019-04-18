package ps.g49.socialroutingservice.exceptions.types

class UnsupportedMediaTypeException(
        val type: String = "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#unsupported-media-type",
        val status: Int = 415,
        val title: String = "Unsupported media type exception."
) : Exception()


