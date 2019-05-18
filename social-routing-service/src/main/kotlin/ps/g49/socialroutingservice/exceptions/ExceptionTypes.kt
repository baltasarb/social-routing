package ps.g49.socialroutingservice.exceptions

/**
 * thrown when a request does not have the correct form
 */
class BadRequestException : Exception()

/**
 * thrown when a resource requested is not present
 */
class ResourceNotFoundException : Exception()

/**
 * thrown when the media type requested is not supported by the API
 */
class UnsupportedMediaTypeException : Exception()