package ps.g49.socialroutingservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ps.g49.socialroutingservice.exceptions.types.ResourceNotFoundException
import ps.g49.socialroutingservice.exceptions.types.UnsupportedMediaTypeException

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [ResourceNotFoundException::class])
    fun handleResourceNotFoundException(exception: ResourceNotFoundException) : ResponseEntity<ProblemJsonResponse> {
        val error = ProblemJsonResponse("Not Found", "Not found title", 404, "some detail", "")
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }

    @ExceptionHandler(value = [UnsupportedMediaTypeException::class])
    fun handleUnsupportedMediaTypeException(exception: UnsupportedMediaTypeException) : ResponseEntity<ProblemJsonResponse> {
        val error = ProblemJsonResponse("Not Found", "Not found title", 404, "some detail", "")
        return ResponseEntity(error, HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    }

}