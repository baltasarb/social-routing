package ps.g49.socialroutingservice.utils

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

class OutputUtils {

    companion object {
        private const val HOST = "http://localhost:8080"

        private const val API_URL: String = "$HOST/api.sr"
        private const val PERSONS_URL: String = "$API_URL/persons"
        private const val ROUTES_URL: String = "$API_URL/routes"

        fun personUrl(personIdentifier: Int): String = "$PERSONS_URL/$personIdentifier"
        fun personRoutesUrl(personIdentifier: Int) = "$PERSONS_URL/$personIdentifier/routes"
        fun routeUrl(routeIdentifier: Int): String = "$ROUTES_URL/$routeIdentifier"

        //used for get responses
        fun <T> ok(body: T): ResponseEntity<T> {
            return ResponseEntity.ok(body)
        }

        //used for DELETE responses
        fun ok(): ResponseEntity<Void> {
            return ResponseEntity
                    .ok()
                    .build()
        }

        //used for post or put responses with empty body
        fun ok(headers: HttpHeaders): ResponseEntity<Void> {
            return ResponseEntity
                    .ok()
                    .headers(headers)
                    .build()
        }

        fun <T> ok (headers: HttpHeaders, body :T) : ResponseEntity<T>{
            return ResponseEntity(body, headers, HttpStatus.OK)
        }
    }

}