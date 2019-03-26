package ps.g49.socialroutingservice.modelMappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.model.Person
import java.sql.ResultSet

@Component
class PersonMapper : RowMapper<Person> {

    override fun map(rs: ResultSet?, ctx: StatementContext?): Person {
        return Person(
                rs!!.getString("FirstName"),
                rs.getString("LastName"),
                rs.getString("Email")
        )
    }

}