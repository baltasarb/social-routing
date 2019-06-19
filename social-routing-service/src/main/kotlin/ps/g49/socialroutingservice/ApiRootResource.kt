package ps.g49.socialroutingservice

import com.fasterxml.jackson.annotation.JsonProperty

data class ApiRootResource (
        @JsonProperty("person_url")
        val personUrl : String = "$PERSONS_URL/{personIdentifier}",
        @JsonProperty("route_search_url")
        val routeSearchUrl : String = "$ROUTES_URL/search?{params}",//todo specific params
        @JsonProperty("categories_url")
        var categoriesUrl : String = CATEGORIES_URL,
        @JsonProperty("authentication_urls")
        var authenticationUrls : HashMap<String, String> = hashMapOf("google" to GOOGLE_AUTHENTICATION_URL),
        @JsonProperty("documentation_url")
        val documentationUrl : String = "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API"
){
    companion object{
        private const val HOST = "http://10.0.2.2:8080"
        private const val API_URL: String = "$HOST/api.sr"
        private const val AUTHENTICATION_URL : String = "$API_URL/authentication"
        private const val PERSONS_URL: String = "$API_URL/persons"
        private const val ROUTES_URL: String = "$API_URL/routes"
        private const val CATEGORIES_URL : String = "$API_URL/categories"
        private const val GOOGLE_AUTHENTICATION_URL : String = "$AUTHENTICATION_URL/google"
    }
}