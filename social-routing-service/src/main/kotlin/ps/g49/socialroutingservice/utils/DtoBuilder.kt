package ps.g49.socialroutingservice.utils

import ps.g49.socialroutingservice.models.dtos.CategoryDto
import ps.g49.socialroutingservice.models.dtos.PersonDto
import ps.g49.socialroutingservice.models.dtos.RouteDto
import ps.g49.socialroutingservice.models.dtos.SearchDto
import ps.g49.socialroutingservice.models.inputModel.PersonInput
import ps.g49.socialroutingservice.models.inputModel.RouteInput

class DtoBuilder {

    companion object {

        fun buildRouteDto(routeInput: RouteInput, id: Int? = null): RouteDto = RouteDto(
                identifier = id,
                location = routeInput.location,
                name = routeInput.name,
                description = routeInput.description,
                rating = routeInput.rating,
                points = routeInput.points,
                personIdentifier = routeInput.personIdentifier,
                dateCreated = routeInput.dateCreated,
                duration = routeInput.duration,
                categories = routeInput.categories.map { CategoryDto(it.name) }
        )

        fun buildPersonDto(personInput: PersonInput, id: Int? = null): PersonDto = PersonDto(
                identifier = id,
                name = personInput.name,
                email = personInput.email,
                rating = personInput.rating
        )

        fun buildSearchDto(params: HashMap<String, String>): SearchDto {
            val page = params["page"]?.toInt()
            return SearchDto(
                    location = params["location"],
                    page = page?:1
            )
        }

    }

}