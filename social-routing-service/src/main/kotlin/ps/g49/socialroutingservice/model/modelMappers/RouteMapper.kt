package ps.g49.socialroutingservice.model.modelMappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.model.Route
import java.sql.ResultSet

@Component
class RouteMapper : RowMapper<Route> {

    override fun map(rs: ResultSet?, ctx: StatementContext?): Route {
        return Route(
                rs!!.getLong("Identifier"),
                rs.getString("PersonIdentifier")
        )
    }

}