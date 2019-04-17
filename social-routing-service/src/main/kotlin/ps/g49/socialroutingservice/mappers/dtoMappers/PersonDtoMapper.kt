package ps.g49.socialroutingservice.mappers.dtoMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.dtos.PersonDto
import ps.g49.socialroutingservice.inputModel.PersonInput
import ps.g49.socialroutingservice.mappers.Mapper

@Component
class PersonDtoMapper : Mapper<PersonInput, PersonDto> {

    override fun map(from: PersonInput): PersonDto = PersonDto(
            name = from.name,
            email = from.email
    )

}