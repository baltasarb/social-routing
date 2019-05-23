package ps.g49.socialroutingservice.services

import com.google.api.client.extensions.appengine.http.UrlFetchTransport
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import com.google.api.client.json.jackson2.JacksonFactory
import java.util.*

class SignInService {

    val CLIENT_ID = "989313558568-hqdfgtllk76149mgf6t627504bojucfg.apps.googleusercontent.com"

    fun signIn(idTokenString : String){

        val idToken = verifyIdToken(idTokenString)
        //check if token is valid
            //if yes
                //check user exists
                    // if not -> create
                // login
            //if not throw authentication exception

        if (idToken != null) {
            val payload = idToken.payload

            // Print user identifier
            val userId = payload.subject
            println("User ID: $userId")

            // Get profile information from payload
            val email = payload.email
            val emailVerified = java.lang.Boolean.valueOf(payload.emailVerified)
            val name = payload["name"] as String
            val pictureUrl = payload["picture"] as String
            val locale = payload["locale"] as String
            val familyName = payload["family_name"] as String
            val givenName = payload["given_name"] as String

            // Use or store profile information
            // ...

        } else {
            println("Invalid ID token.")
        }
    }

    private fun verifyIdToken(idTokenString: String) : GoogleIdToken?{
        val jacksonFactory = JacksonFactory()
        val transport = UrlFetchTransport.getDefaultInstance()

        val verifier = GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build()

        return verifier.verify(idTokenString)
    }



}