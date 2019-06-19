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

    fun handleInvalidGoogleTokenException(exception: Exception): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "",
                "Unauthorized",
                401,
                "Invalid google credentials. The access token you're using is either expired or invalid",
                ""
        )
        return ResponseEntity(error, HttpStatus.UNAUTHORIZED)
    }

    fun handleAuthenticationException(exception: Exception): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "",
                "Unauthorized",
                401,
                "Invalid authorization header. The access token you're using is either expired or invalid",
                ""
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