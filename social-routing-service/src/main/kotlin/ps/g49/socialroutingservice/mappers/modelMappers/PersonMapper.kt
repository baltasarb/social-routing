package ps.g49.socialroutingservice.mappers.modelMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.dtos.PersonDto
import ps.g49.socialroutingservice.model.Person
import java.sql.ResultSet

@Component
class PersonMapper : ModelMapper<PersonDto, Person> {

    override fun map(from: PersonDto): Person {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun mapFromResultSet(rs: ResultSet): Person = Person(
            identifier = rs.getInt("Identifier"),
            name = rs.getString("Name"),
            email = rs.getString("Email")
    )
}