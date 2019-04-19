package ps.g49.socialroutingservice.mappers.modelMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.dtos.PersonDto
import ps.g49.socialroutingservice.models.domainModel.Person
import java.sql.ResultSet

@Component
class PersonMapper : ModelMapper<PersonDto, Person> {

    override fun map(from: PersonDto): Person = Person(
            identifier = from.identifier,
            name = from.name,
            email = from.email,
            rating = from.rating
    )

    override fun mapFromResultSet(rs: ResultSet): Person = Person(
            identifier = rs.getInt("Identifier"),
            name = rs.getString("Name"),
            email = rs.getString("Email"),
            rating = rs.getDouble("Rating")
    )

}