package ps.g49.socialroutingservice

import ps.g49.socialroutingservice.models.dtos.RouteDto
import ps.g49.socialroutingservice.models.inputModel.RouteInput

class DtoBuilder {

    companion object{

        fun buildRouteDto(routeInput: RouteInput, id : Int? = null): RouteDto = RouteDto(
                identifier = id,
                location = routeInput.location,
                name = routeInput.name,
                description = routeInput.description,
                points = routeInput.points,
                personIdentifier = routeInput.personIdentifier
        )

    }

}