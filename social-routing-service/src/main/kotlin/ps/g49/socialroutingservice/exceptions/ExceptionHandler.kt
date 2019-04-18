package ps.g49.socialroutingservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ps.g49.socialroutingservice.exceptions.types.ResourceNotFoundException
import ps.g49.socialroutingservice.exceptions.types.UnsupportedMediaTypeException
import ps.g49.socialroutingservice.models.outputModel.ProblemJson
import ps.g49.socialroutingservice.exceptions.types.BadRequestException
import java.lang.Exception

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [ResourceNotFoundException::class])
    fun handleResourceNotFoundException(exception: ResourceNotFoundException) : ResponseEntity<ProblemJson> {
        val error = ProblemJson("Not Found", "Not found title", 404, "some detail", "")
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [UnsupportedMediaTypeException::class])
    fun handleUnsupportedMediaTypeException(exception: UnsupportedMediaTypeException) : ResponseEntity<ProblemJson> {
        val error = ProblemJson(exception.type, exception.title, exception.status, "", "")
        return ResponseEntity(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    }

    @ExceptionHandler(value = [BadRequestException::class, MethodArgumentTypeMismatchException::class])
    fun handleBadRequestException(exception: Exception) : ResponseEntity<ProblemJson> {
        val error = ProblemJson("", "a", 1, "","")
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }
}