package ps.g49.socialroutingservice.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ps.g49.socialroutingservice.exceptions.types.JsonResponseException
import ps.g49.socialroutingservice.exceptions.types.ResourceNotFoundException

@ControllerAdvice
class ExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [ResourceNotFoundException::class])
    fun handleResourceNotFoundException(exception: ResourceNotFoundException) : ResponseEntity<JsonResponseException> {
        val error = ProblemJsonResponse("Not Found", "Not found title", 404, "some detail", "")
        return ResponseEntity(exception, HttpStatus.NOT_FOUND)
    }

}