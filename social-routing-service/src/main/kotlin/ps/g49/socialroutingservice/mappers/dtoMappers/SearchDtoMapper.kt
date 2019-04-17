package ps.g49.socialroutingservice.mappers.dtoMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.dtos.SearchDto
import ps.g49.socialroutingservice.inputModel.SearchInput

@Component
class SearchDtoMapper : DtoMapper<SearchInput, SearchDto> {

    override fun map(from: SearchInput): SearchDto = SearchDto(
            location = from.location!!
    )

}