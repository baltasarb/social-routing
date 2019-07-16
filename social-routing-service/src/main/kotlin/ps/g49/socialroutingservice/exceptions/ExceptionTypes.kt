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

/**
 * thrown when an invalid google token is received
 */
class GoogleTokenInvalidException : Exception()

/**
 * thrown when the authorization header is not present
 */
class AuthorizationHeaderException : Exception()

/**
 * thrown when the refresh token is invalid or not present
 */
class InvalidRefreshTokenException : Exception()

/**
 * thrown when a refresh is attempted but the corresponding access token is still valid
 */
class RefreshNotAllowedException : Exception()

/**
 * thrown when an access token is expired
 */
class TokenExpiredException : Exception()

/**
 * thrown when the provided access token is invalid
 */
class InvalidAccessTokenException : Exception()

/**
 * thrown when the media type is undefined in the request
 */
class MediaTypeNotPresentException : Exception()

/**
 * thrown when one of the search parameters is not present or in an invalid format
 */
class InvalidRouteSearchParameterException(override val message: String) : Exception()

/**
 * thrown when an uknown error occurs
 */
class InternalServerErrorException : Exception()

/**
 * thrown when a user attempts to change other user's data
 */
class ForbiddenRequestException : Exception()

/**
 * thrown when a user attempts to create or update a route but does not provide any categories
 */
class RouteCategoriesRequiredException : Exception()