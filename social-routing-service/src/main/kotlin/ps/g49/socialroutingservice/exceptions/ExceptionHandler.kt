package ps.g49.socialroutingservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ps.g49.socialroutingservice.models.outputModel.ProblemJson
import java.lang.Exception
import java.sql.SQLException

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [ResourceNotFoundException::class])
    fun handleResourceNotFoundException(exception: ResourceNotFoundException): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#not-found",
                "Not Found",
                404,
                "brief description of instance problem",
                "link to full description"
        )
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [UnsupportedMediaTypeException::class])
    fun handleUnsupportedMediaTypeException(exception: UnsupportedMediaTypeException): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#unsupported-media-type",
                "Unsupported Media Type",
                415,
                "brief description of instance problem",
                "link to full description"
        )
        return ResponseEntity(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    }

    @ExceptionHandler(value = [BadRequestException::class, MethodArgumentTypeMismatchException::class])
    fun handleBadRequestException(exception: Exception): ResponseEntity<ProblemJson> {
        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#bad-request",
                "Bad Request.",
                400,
                "brief description of instance problem",
                "link to full description"
        )
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

   /* @ExceptionHandler(value= [SQLException::class])
    fun handleSqlException(exception : SQLException) : ResponseEntity<ProblemJson>{

        val error = ProblemJson(
                "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API#bad-request",
                "Internal Server Error.",
                500,
                "brief description of instance problem",
                "link to full description"
        )
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)

    }
*/
    //fun handleJson
}