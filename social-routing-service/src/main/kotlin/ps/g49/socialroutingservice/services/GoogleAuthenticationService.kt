package ps.g49.socialroutingservice.services

import com.google.api.client.extensions.appengine.http.UrlFetchTransport
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.json.jackson2.JacksonFactory
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.models.domainModel.GooglePersonInfo
import ps.g49.socialroutingservice.repositories.AuthenticationRepository
import java.util.Collections.*


@Service
class GoogleAuthenticationService(
        private val authenticationRepository: AuthenticationRepository
) {

    val CLIENT_ID = "989313558568-hqdfgtllk76149mgf6t627504bojucfg.apps.googleusercontent.com"

    fun validateServerGeneratedTokenAndSubject(hashedToken : String, subject : String) : Boolean {
        return authenticationRepository.validateServerGeneratedTokenAndSubject(hashedToken, subject)
    }

    fun validateAndGetIdToken(idTokenString: String): GoogleIdToken? {
        val jacksonFactory = JacksonFactory()
        val transport = UrlFetchTransport.getDefaultInstance()

        val verifier = GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(singletonList(CLIENT_ID))
                .build()

        return verifier.verify(idTokenString)
    }

}