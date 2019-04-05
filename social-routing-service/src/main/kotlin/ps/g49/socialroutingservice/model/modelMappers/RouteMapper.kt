package ps.g49.socialroutingservice.model.modelMappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.model.Point
import ps.g49.socialroutingservice.model.PointCollection
import ps.g49.socialroutingservice.model.Route
import java.sql.ResultSet
import java.util.*

@Component
class RouteMapper : RowMapper<Route> {

    override fun map(rs: ResultSet?, ctx: StatementContext?): Route {
        val json = rs!!.getObject("Points")

        println("test")
        return Route(
                identifier = rs.getLong("Identifier"),
                location = rs.getString("Location"),
                name = rs.getString("Name"),
                description = rs.getString("Description"),
                classification = rs.getDouble("Classification"),
                duration = rs.getLong("Duration"),
                dateCreated = rs.getDate("DateCreated"),
                points = PointCollection(Arrays.asList()),//TODO
                personIdentifier = rs.getInt("PersonIdentifier")
        )
    }

}