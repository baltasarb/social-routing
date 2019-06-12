package ps.g49.socialroutingservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.mappers.outputMappers.AuthenticationDataOutputMapper
import ps.g49.socialroutingservice.models.AuthenticationData
import ps.g49.socialroutingservice.models.outputModel.AuthenticationDataOutput
import ps.g49.socialroutingservice.services.AuthenticationService
import ps.g49.socialroutingservice.services.GoogleAuthenticationService
import ps.g49.socialroutingservice.services.PersonService
import ps.g49.socialroutingservice.utils.OutputUtils

@RestController
@RequestMapping("/authentication")
class AuthenticationController (
        private val authenticationService: AuthenticationService,
        private val googleAuthenticationService: GoogleAuthenticationService,
        private val connectionManager: ConnectionManager,
        private val personService: PersonService,
        private val authenticationDataOutputMapper: AuthenticationDataOutputMapper
){

    @PostMapping("/google")
    fun googleSignIn(@RequestParam idTokenString: String): ResponseEntity<AuthenticationDataOutput> {
     //   val googleIdToken = googleAuthenticationService.validateAndGetIdToken(idTokenString) ?: throw GoogleAuthenticationException()

        val connectionHandle = connectionManager.generateHandle()

        //transaction
     //   val subject = googleIdToken.payload.subject

        val subject = "sub 3"

        var personIdentifier = googleAuthenticationService.getPersonIdIfExists(connectionHandle, subject)

        if(personIdentifier == null)
            personIdentifier = personService.createPerson(connectionHandle)

        var authenticationData : AuthenticationData? = authenticationService.getPersonAuthenticationData(connectionHandle, personIdentifier)

        if(authenticationData == null){
            authenticationData = authenticationService.generateAuthenticationData(personIdentifier)

            val hashedAuthenticationData = authenticationService.hashAuthenticationDataAndGet(authenticationData)

            authenticationService.storeHashedAuthenticationData(connectionHandle, hashedAuthenticationData)
            //might already exist
            googleAuthenticationService.storeGoogleAuthenticationData(connectionHandle, subject, personIdentifier)
        }

        connectionHandle.close()

        val output = authenticationDataOutputMapper.map(authenticationData)
        return OutputUtils.ok(output)
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestParam refreshToken : String){

    }

   /* fun validateServerGeneratedTokenAndSubject(hashedToken : String, subject : String) : Boolean {
        return authenticationRepository.validateServerGeneratedTokenAndSubject(hashedToken, subject)
    }*/
}