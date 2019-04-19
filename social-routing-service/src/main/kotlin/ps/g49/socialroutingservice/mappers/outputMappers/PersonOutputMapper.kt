package ps.g49.socialroutingservice.mappers.outputMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.Person
import ps.g49.socialroutingservice.models.outputModel.PersonOutput
import ps.g49.socialroutingservice.utils.OutputUtils

@Component
class PersonOutputMapper : OutputMapper<Person, PersonOutput> {

    override fun map(from: Person): PersonOutput {
        val id = from.identifier!!
        return PersonOutput(
                identifier = id,
                name = from.name,
                email = from.email,
                rating = from.rating!!,
                routesUrl = OutputUtils.personRoutesUrl(id)
        )
    }

}