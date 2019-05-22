package ps.g49.socialroutingservice

data class ApiRootResource (
        val personUrl : String = "$PERSONS_URL/{personIdentifier}",
        val routeSearchUrl : String = "$ROUTES_URL/search?{params}",
        val documentationUrl : String = "https://github.com/baltasarb/social-routing/wiki/Social-Routing-API"
){
    companion object{
        private const val HOST = "http://10.0.2.2:8080/api.sr/"
        private const val API_URL: String = "$HOST/api.sr"
        private const val PERSONS_URL: String = "$API_URL/persons"
        private const val ROUTES_URL: String = "$API_URL/routes"
    }
}