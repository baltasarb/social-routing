package ps.g49.socialroutingservice.mappers.modelMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.requests.PersonRequest
import ps.g49.socialroutingservice.models.domainModel.Person
import java.sql.ResultSet

@Component
class PersonMapper : ModelMapper<PersonRequest, Person> {

    override fun map(from: PersonRequest): Person = Person(
            identifier = from.identifier,
            rating = from.rating
    )

    override fun mapFromResultSet(rs: ResultSet): Person = Person(
            identifier = rs.getInt("Identifier"),
            rating = rs.getDouble("Rating")
    )

}