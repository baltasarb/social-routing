package ps.g49.socialroutingservice.services

//import com.google.api.client.extensions.appengine.http.UrlFetchTransport
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.http.HttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.json.jackson2.JacksonFactory
import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.repositories.GoogleAuthenticationRepository
import ps.g49.socialroutingservice.repositories.PersonRepository
import java.lang.Exception
import java.util.Collections.*


@Service
class GoogleAuthenticationService(
        private val googleAuthenticationRepository: GoogleAuthenticationRepository
) {

    val CLIENT_ID = "989313558568-hh544t4t551d4dd04amadvj3sft9hj0t.apps.googleusercontent.com"

    fun validateAndGetIdToken(idTokenString: String): GoogleIdToken? {
        val jacksonFactory = JacksonFactory()
        //val transport = UrlFetchTransport.getDefaultInstance()
        val transport = NetHttpTransport.Builder().build()

        val verifier = GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
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