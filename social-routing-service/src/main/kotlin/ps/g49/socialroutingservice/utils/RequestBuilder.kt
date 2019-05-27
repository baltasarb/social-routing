package ps.g49.socialroutingservice.utils

import ps.g49.socialroutingservice.models.inputModel.PersonInput
import ps.g49.socialroutingservice.models.inputModel.RouteInput
import ps.g49.socialroutingservice.models.requests.*

class RequestBuilder {

    companion object {

        fun buildRouteRequest(routeInput: RouteInput, id: Int? = null): RouteRequest = RouteRequest(
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
                rating = personInput.rating
        )

        fun buildSearchRequest(params: HashMap<String, String>): SearchRequest {
            val page = params["page"]?.toInt()
            return SearchRequest(
                    location = params["location"],
                    page = page?:1
            )
        }

        fun buildUserRoutesRequest(identifier: Int, params: HashMap<String, String>): UserRoutesRequest {
            val page = params["page"]?.toInt()
            return UserRoutesRequest(
                    identifier = identifier,
                    page = page?:1
            )
        }

    }

}