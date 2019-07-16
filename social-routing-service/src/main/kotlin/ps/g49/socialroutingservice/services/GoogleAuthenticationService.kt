package ps.g49.socialroutingservice.services

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.repositories.GoogleAuthenticationRepository
import java.util.Collections.*

@Service
class GoogleAuthenticationService(
        private val googleAuthenticationRepository: GoogleAuthenticationRepository
) {

    companion object {
        //the client application identifier in the google api service
        private const val CLIENT_ID = "989313558568-hh544t4t551d4dd04amadvj3sft9hj0t.apps.googleusercontent.com"
    }

    //validates the received id token string using a google library
    fun validateAndGetIdToken(idTokenString: String): GoogleIdToken? {
        val jacksonFactory = JacksonFactory()
        val transport = NetHttpTransport.Builder().build()

        val verifier = GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                .setAudience(singletonList(CLIENT_ID))
                .setIssuer("https://accounts.google.com")
                .build()

        return verifier.verify(idTokenString)
    }

    fun getPersonIdWithSub(connectionHandle: Handle, subject: String): Int? {
        return googleAuthenticationRepository.findPersonIdBySub(connectionHandle, subject)
    }

    fun storeGoogleAuthenticationData(connectionHandle: Handle, subject: String, personIdentifier: Int) {
        googleAuthenticationRepository.create(connectionHandle, subject, personIdentifier)
    }

}