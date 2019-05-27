package ps.g49.socialroutingservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ps.g49.socialroutingservice.models.outputModel.GooglePersonInfoOutput
import ps.g49.socialroutingservice.services.AuthenticationService
import ps.g49.socialroutingservice.services.GoogleAuthenticationService
import ps.g49.socialroutingservice.utils.OutputUtils

@RestController
@RequestMapping("/sign-in")
class AuthenticationController (
        private val authenticationService: AuthenticationService
){

    @PostMapping("/google")
    fun googleSignIn(@RequestParam idTokenString: String): ResponseEntity<GooglePersonInfoOutput> {
        val googlePersonInfo = authenticationService.googleSignIn(idTokenString)
        val output = GooglePersonInfoOutput(googlePersonInfo.identifier, googlePersonInfo.token)
        return OutputUtils.ok(output)
    }

}