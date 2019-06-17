package ps.g49.socialroutingservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.exceptions.GoogleAuthenticationException
import ps.g49.socialroutingservice.exceptions.InvalidRefreshTokenException
import ps.g49.socialroutingservice.exceptions.RefreshNotAllowedException
import ps.g49.socialroutingservice.mappers.outputMappers.AuthenticationDataOutputMapper
import ps.g49.socialroutingservice.models.inputModel.GoogleRegistrationInput
import ps.g49.socialroutingservice.models.inputModel.RefreshAuthenticationDataInput
import ps.g49.socialroutingservice.models.outputModel.AuthenticationDataOutput
import ps.g49.socialroutingservice.services.AuthenticationService
import ps.g49.socialroutingservice.services.GoogleAuthenticationService
import ps.g49.socialroutingservice.services.PersonService
import ps.g49.socialroutingservice.utils.OutputUtils

@RestController
@RequestMapping("/authentication")
class AuthenticationController(
        private val authenticationService: AuthenticationService,
        private val googleAuthenticationService: GoogleAuthenticationService,
        private val connectionManager: ConnectionManager,
        private val personService: PersonService,
        private val authenticationDataOutputMapper: AuthenticationDataOutputMapper
) {

    @PostMapping("/google")
    fun googleRegistration(@RequestBody googleRegistrationInput: GoogleRegistrationInput): ResponseEntity<AuthenticationDataOutput> {
        val googleIdToken = googleAuthenticationService.validateAndGetIdToken(googleRegistrationInput.idTokenString)
                ?: throw GoogleAuthenticationException()

        val subject = googleIdToken.payload.subject

        val connectionHandle = connectionManager.generateHandle()

        //transaction

        var personIdentifier = googleAuthenticationService.getPersonIdWithSub(connectionHandle, subject)

        //if the person does not exist, create a new one and add the google authentication to it
        if (personIdentifier == null){
            personIdentifier = personService.createPerson(connectionHandle)
            googleAuthenticationService.storeGoogleAuthenticationData(connectionHandle, subject, personIdentifier)
        }

        val authenticationData = authenticationService.generateAuthenticationData(personIdentifier)
        val hashedAuthenticationData = authenticationService.hashAuthenticationDataAndGet(authenticationData)

        authenticationService.storeHashedAuthenticationData(connectionHandle, hashedAuthenticationData)

        connectionHandle.close()

        val output = authenticationDataOutputMapper.map(authenticationData)
        return OutputUtils.ok(output)
    }

    @PostMapping("/refresh")
    fun refreshToken(@RequestBody refreshAuthenticationDataInput: RefreshAuthenticationDataInput): ResponseEntity<AuthenticationDataOutput> {
        val handle = connectionManager.generateHandle()

        val hashedRefreshToken = authenticationService.hashTokenToSHA256(refreshAuthenticationDataInput.refreshToken)

        //try to retrieve the user authentication data and throw an exception if not present
        val retrievedAuthenticationData =
                authenticationService.getPersonAuthenticationDataByRefreshToken(handle, hashedRefreshToken)
                        ?: throw InvalidRefreshTokenException()

        //check if a refresh is required/possible
        if (retrievedAuthenticationData.accessTokenIsExpired()) {
            throw RefreshNotAllowedException()
        }

        //create the new data
        val newAuthenticationData = authenticationService.generateAuthenticationData(retrievedAuthenticationData.personIdentifier)

        //hash the new data
        val newAuthenticationDataHashed = authenticationService.hashAuthenticationDataAndGet(newAuthenticationData)

        //store the new data
        authenticationService.storeHashedAuthenticationData(handle, newAuthenticationDataHashed)

        handle.close()

        val output = authenticationDataOutputMapper.map(newAuthenticationData)
        return OutputUtils.ok(output)
    }

}