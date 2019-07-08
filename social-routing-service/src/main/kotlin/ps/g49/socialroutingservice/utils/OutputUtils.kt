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
        private const val SEARCH_ROUTES_URL = "$ROUTES_URL/search?"

        fun personUrl(personIdentifier: Int): String = "$PERSONS_URL/$personIdentifier"
        fun personRoutesUrl(personIdentifier: Int) = "$PERSONS_URL/$personIdentifier/routes"
        fun routeUrl(routeIdentifier: Int): String = "$ROUTES_URL/$routeIdentifier"
        fun personRoutesUrlWithPage(personIdentifier: Int, page : Int) : String = personRoutesUrl(personIdentifier) + "?page=$page"

        fun searchRoutesUrlWithParams(params : HashMap<String,String>, page : Int) : String {
            val stringBuilder = StringBuilder()

            stringBuilder.append(SEARCH_ROUTES_URL)
            stringBuilder.append("page=$page")

            val latitude : String? = params["latitude"]
            val longitude : String? = params["longitude"]

            if((latitude != null) and (longitude != null)) {
                stringBuilder.append("&latitude=$latitude")
                stringBuilder.append("&longitude=$longitude")
            }

            val location = params["location"]
            stringBuilder.append("&location=$location")

            val categories = params["categories"]
            stringBuilder.append("&categories=$categories")

            val duration = params["duration"]
            stringBuilder.append("&duration=$duration")

            return stringBuilder.toString()
        }

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