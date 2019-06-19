package ps.g49.socialroutingservice.exceptions

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.context.request.WebRequest
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.NoHandlerFoundException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ps.g49.socialroutingservice.models.outputModel.ProblemJson
import java.lang.Exception
import java.lang.IllegalStateException
import java.sql.SQLException

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [ResourceNotFoundException::class, IllegalStateException::class])
    fun handleResourceNotFoundException(exception: Exception): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#not-found",
                HttpStatus.NOT_FOUND.reasonPhrase,
                HttpStatus.NOT_FOUND.value(),
                "Resource not found",
                "link to full description"
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [UnsupportedMediaTypeException::class])
    fun handleUnsupportedMediaTypeException(exception: UnsupportedMediaTypeException): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#unsupported-media-type",
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.reasonPhrase,
                HttpStatus.UNSUPPORTED_MEDIA_TYPE.value(),
                "brief description of instance problem",
                "link to full description"
        )
        return ResponseEntity(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    }

    @ExceptionHandler(value = [MediaTypeNotPresentException::class])
    fun handleMediaTypeNotPresentException(exception: Exception): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#media-type-header-not-present",
                HttpStatus.BAD_REQUEST.reasonPhrase,
                HttpStatus.BAD_REQUEST.value(),
                "Media type header missing"
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [AuthorizationHeaderException::class])
    fun handleAuthorizationHeaderNotPresentException(exception: Exception): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#authorization-header-not-present",
                HttpStatus.BAD_REQUEST.reasonPhrase,
                HttpStatus.BAD_REQUEST.value(),
                "Authorization header missing"
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(value = [GoogleTokenInvalidException::class])
    fun handleInvalidGoogleTokenException(exception: Exception): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#google-idTokenString-invalid",
                HttpStatus.UNAUTHORIZED.reasonPhrase,
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid google credentials. The access token you're using is either expired or invalid"
        )
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(value = [InvalidAccessTokenException::class])
    fun handleInvalidAccessTokenException(exception: Exception): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#accessToken-invalid",
                HttpStatus.UNAUTHORIZED.reasonPhrase,
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid access token. The access token you're using is either expired or invalid"
        )
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(value = [InvalidRefreshTokenException::class])
    fun handleInvalidRefreshTokenException(exception: Exception): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#refreshToken-invalid",
                HttpStatus.UNAUTHORIZED.reasonPhrase,
                HttpStatus.UNAUTHORIZED.value(),
                "Invalid refresh token. The refresh token you're using is either expired or invalid"
        )
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    override fun handleHttpMessageNotReadable(ex: HttpMessageNotReadableException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#bad-request",
                HttpStatus.BAD_REQUEST.reasonPhrase,
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.reasonPhrase,
                "link to full description"
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [BadRequestException::class, MethodArgumentTypeMismatchException::class])
    fun handleBadRequestException(exception: Exception): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#bad-request",
                HttpStatus.BAD_REQUEST.reasonPhrase,
                HttpStatus.BAD_REQUEST.value(),
                "brief description of instance problem",
                "link to full description"
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

}