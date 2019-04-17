package ps.g49.socialroutingservice.mappers.dtoMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.dtos.SearchDto

@Component
class SearchDtoMapper : DtoMapper<HashMap<String, String>, SearchDto> {

    override fun map(from: HashMap<String, String>): SearchDto = SearchDto(
            location = from["location"]!!
    )

}