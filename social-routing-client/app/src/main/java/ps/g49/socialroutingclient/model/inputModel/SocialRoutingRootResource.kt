package ps.g49.socialroutingclient.model.inputModel

import com.fasterxml.jackson.annotation.JsonProperty

data class SocialRoutingRootResource(
    @JsonProperty("person_url")
    val personUrl: String,
    @JsonProperty("route_search_url")
    val routeSearchUrl: String,
    @JsonProperty("categories_url")
    var categoriesUrl: String,
    @JsonProperty("authentication_urls")
    var authenticationUrls: HashMap<String, String>,
    @JsonProperty("documentation_url")
    val documentationUrl: String

)