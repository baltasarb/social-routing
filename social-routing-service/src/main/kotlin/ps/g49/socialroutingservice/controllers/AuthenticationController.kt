package ps.g49.socialroutingservice.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.exceptions.GoogleTokenInvalidException
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

/**
 * class responsible for every endpoint related to a route
 */
@RestController
@RequestMapping("/authentication")
class AuthenticationController(
        private val authenticationService: AuthenticationService,
        private val googleAuthenticationService: GoogleAuthenticationService,
        private val connectionManager: ConnectionManager,
        private val personService: PersonService,
        private val authenticationDataOutputMapper: AuthenticationDataOutputMapper
) {

    /**
     * enpoint used to retrieve valid credentials to further access the API
     * @param googleRegistrationInput should contain a valid id token string obtained from the google sign in api
     * @return an object containing a valid access token and and refresh token
     */
    @PostMapping("/google")
    fun googleRegistration(@RequestBody googleRegistrationInput: GoogleRegistrationInput): ResponseEntity<AuthenticationDataOutput> {
        val googleIdToken = googleAuthenticationService.validateAndGetIdToken(googleRegistrationInput.idTokenString)
                ?: throw GoogleTokenInvalidException()

        val subject = googleIdToken.payload.subject

        val connectionHandle = connectionManager.generateHandle()

        connectionHandle.use {
            var personIdentifier = googleAuthenticationService.getPersonIdWithSub(it, subject)

            //if the person does not exist, create a new one and add the google authentication to it
            if (personIdentifier == null) {
                personIdentifier = personService.createPerson(it)
                googleAuthenticationService.storeGoogleAuthenticationData(it, subject, personIdentifier)
            }

            val authenticationData = authenticationService.generateAuthenticationData(personIdentifier)
            val hashedAuthenticationData = authenticationService.hashAuthenticationDataAndGet(authenticationData)

            authenticationService.storeHashedAuthenticationData(it, hashedAuthenticationData)

            val output = authenticationDataOutputMapper.map(authenticationData)
            val headers = HttpHeaders()
            headers.set("Location", OutputUtils.personUrl(personIdentifier))
            return OutputUtils.ok(headers, output)
        }
    }

    /**
     * endpoint used to refresh the access token belonging to a user
     * @param refreshAuthenticationDataInput an object containing a valid refresh token used to refresh the corresponding access token
     * @return an object containing a valid access token and and refresh token
     */
    @PostMapping("/refresh")
    fun refreshToken(@RequestBody refreshAuthenticationDataInput: RefreshAuthenticationDataInput): ResponseEntity<AuthenticationDataOutput> {
        val connectionHandle = connectionManager.generateHandle()
        connectionHandle.use {
            val hashedRefreshToken = authenticationService.hashTokenToSHA256(refreshAuthenticationDataInput.refreshToken)

            //try to retrieve the user authentication data and throw an exception if not present
            val retrievedAuthenticationData =
                    authenticationService.getPersonAuthenticationDataByRefreshToken(it, hashedRefreshToken)
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
            authenticationService.storeHashedAuthenticationData(it, newAuthenticationDataHashed)

            val output = authenticationDataOutputMapper.map(newAuthenticationData)
            return OutputUtils.ok(output)
        }
    }

}