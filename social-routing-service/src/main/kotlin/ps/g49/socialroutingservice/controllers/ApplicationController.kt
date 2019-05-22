package ps.g49.socialroutingservice.controllers

import com.google.api.client.extensions.appengine.http.UrlFetchTransport
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController
import ps.g49.socialroutingservice.ApiRootResource
import ps.g49.socialroutingservice.utils.OutputUtils
import java.util.Collections.singletonList
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier
import java.util.*
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken
import org.springframework.web.bind.annotation.RequestParam
import com.google.api.client.json.jackson2.JacksonFactory




@RestController
class ApplicationController {

    val CLIENT_ID = ""
    @GetMapping
    fun index() : ResponseEntity<ApiRootResource>{
        return OutputUtils.ok(ApiRootResource())
    }

    @PostMapping("/signin")
    fun signIn(@RequestParam idTokenString : String){

        val jacksonFactory = JacksonFactory()
        val transport = UrlFetchTransport.getDefaultInstance()

        val verifier = GoogleIdTokenVerifier.Builder(transport, jacksonFactory)
                // Specify the CLIENT_ID of the app that accesses the backend:
                .setAudience(Collections.singletonList(CLIENT_ID))
                // Or, if multiple clients access the backend:
                //.setAudience(Arrays.asList(CLIENT_ID_1, CLIENT_ID_2, CLIENT_ID_3))
                .build()


        val idToken = verifier.verify(idTokenString)
        if (idToken != null) {
            val payload = idToken!!.getPayload()

            // Print user identifier
            val userId = payload.getSubject()
            println("User ID: $userId")

            // Get profile information from payload
            val email = payload.getEmail()
            val emailVerified = java.lang.Boolean.valueOf(payload.getEmailVerified())
            val name = payload.get("name") as String
            val pictureUrl = payload.get("picture") as String
            val locale = payload.get("locale") as String
            val familyName = payload.get("family_name") as String
            val givenName = payload.get("given_name") as String

            // Use or store profile information
            // ...

        } else {
            println("Invalid ID token.")
        }
    }

}