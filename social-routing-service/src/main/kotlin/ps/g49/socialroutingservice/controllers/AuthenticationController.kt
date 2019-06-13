package ps.g49.socialroutingservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.exceptions.GoogleAuthenticationException
import ps.g49.socialroutingservice.exceptions.InvalidAuthenticationDataException
import ps.g49.socialroutingservice.exceptions.RefreshNotAllowedException
import ps.g49.socialroutingservice.mappers.outputMappers.AuthenticationDataOutputMapper
import ps.g49.socialroutingservice.models.domainModel.AuthenticationData
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
    fun googleSignIn(@RequestParam idTokenString: String): ResponseEntity<AuthenticationDataOutput> {
        val googleIdToken = googleAuthenticationService.validateAndGetIdToken(idTokenString)
                ?: throw GoogleAuthenticationException()
        val subject = googleIdToken.payload.subject

        val connectionHandle = connectionManager.generateHandle()

        //transaction

        var personIdentifier = googleAuthenticationService.getPersonIdIfExists(connectionHandle, subject)

        if (personIdentifier == null)
            personIdentifier = personService.createPerson(connectionHandle)

        var authenticationData: AuthenticationData? = authenticationService.getPersonAuthenticationData(connectionHandle, personIdentifier)

        if (authenticationData == null) {
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
    fun refreshToken(@RequestBody refreshAuthenticationDataInput: RefreshAuthenticationDataInput): ResponseEntity<AuthenticationDataOutput> {
        val handle = connectionManager.generateHandle()

        val authenticationDataReceived = AuthenticationData(0, 0, refreshAuthenticationDataInput.accessToken, refreshAuthenticationDataInput.refreshToken, refreshAuthenticationDataInput.personIdentifier)
        val hashedAuthenticationDataReceived = authenticationService.hashAuthenticationDataAndGet(authenticationDataReceived)

        //try to retrieve the user authentication data and throw an exception if not present
        val retrievedAuthenticationData =
                authenticationService.getPersonAuthenticationData(handle, refreshAuthenticationDataInput.personIdentifier)
                        ?: throw InvalidAuthenticationDataException()

        //check if received data is valid
        if (hashedAuthenticationDataReceived != retrievedAuthenticationData) {
            throw InvalidAuthenticationDataException()
        }

        //check if a refresh is necessary
        if (retrievedAuthenticationData.accessTokenIsExpired()) {
            throw RefreshNotAllowedException()
        }

        //create the new data
        val newAuthenticationData = authenticationService.generateAuthenticationData(refreshAuthenticationDataInput.personIdentifier)

        //hash the new data
        val newAuthenticationDataHashed = authenticationService.hashAuthenticationDataAndGet(newAuthenticationData)

        //store the new data
        authenticationService.storeHashedAuthenticationData(handle, newAuthenticationDataHashed)

        handle.close()
        val output = authenticationDataOutputMapper.map(newAuthenticationData)
        return OutputUtils.ok(output)
    }

}