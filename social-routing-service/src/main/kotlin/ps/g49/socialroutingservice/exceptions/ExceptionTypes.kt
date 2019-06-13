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

/**
 * thrown when an SQL insert fails
 */
class InsertException : Exception()

class GoogleAuthenticationException : Exception()

class AuthorizationHeaderException : Exception()

class AuthorizationException : Exception()

class InvalidAuthenticationDataException : Exception()

class RefreshNotAllowedException : Exception()

class TokenExpiredException : Exception()

class AccessForbiddenException() : Exception()