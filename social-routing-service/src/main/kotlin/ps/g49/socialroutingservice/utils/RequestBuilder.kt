package ps.g49.socialroutingservice.utils

import ps.g49.socialroutingservice.models.requests.CategoryRequest
import ps.g49.socialroutingservice.models.requests.PersonRequest
import ps.g49.socialroutingservice.models.requests.RouteRequest
import ps.g49.socialroutingservice.models.requests.SearchRequest
import ps.g49.socialroutingservice.models.inputModel.PersonInput
import ps.g49.socialroutingservice.models.inputModel.RouteInput

class RequestBuilder {

    companion object {

        fun buildRouteDto(routeInput: RouteInput, id: Int? = null): RouteRequest = RouteRequest(
                identifier = id,
                location = routeInput.location,
                name = routeInput.name,
                description = routeInput.description,
                rating = routeInput.rating,
                points = routeInput.points,
                personIdentifier = routeInput.personIdentifier,
                dateCreated = routeInput.dateCreated,
                duration = routeInput.duration,
                categories = routeInput.categories.map { CategoryRequest(it.name) }
        )

        fun buildPersonDto(personInput: PersonInput, id: Int? = null): PersonRequest = PersonRequest(
                identifier = id,
                name = personInput.name,
                email = personInput.email,
                rating = personInput.rating
        )

        fun buildSearchDto(params: HashMap<String, String>): SearchRequest {
            val page = params["page"]?.toInt()
            return SearchRequest(
                    location = params["location"],
                    page = page?:1
            )
        }

    }

}