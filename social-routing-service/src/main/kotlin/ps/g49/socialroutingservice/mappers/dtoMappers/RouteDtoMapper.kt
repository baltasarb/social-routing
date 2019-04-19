package ps.g49.socialroutingservice.mappers.dtoMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.dtos.RouteDto
import ps.g49.socialroutingservice.models.inputModel.RouteInput

@Component
class RouteDtoMapper : DtoMapper<RouteInput, RouteDto> {

    override fun map(from: RouteInput): RouteDto = RouteDto(
            identifier = from.identifier,
            location = from.location,
            name = from.name,
            description = from.description,
            points = from.points,
            personIdentifier = from.personIdentifier
    )

}