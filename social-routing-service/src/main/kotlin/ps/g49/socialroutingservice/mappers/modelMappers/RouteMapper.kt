package ps.g49.socialroutingservice.mappers.modelMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.dtos.RouteDto
import ps.g49.socialroutingservice.model.PointCollection
import ps.g49.socialroutingservice.model.Route
import java.sql.ResultSet
import java.util.*

@Component
class RouteMapper : ModelMapper<RouteDto, Route> {

    override fun mapFromResultSet(rs: ResultSet): Route = Route(
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

    override fun map(from: RouteDto): Route =
            Route(
                    location = from.location,
                    name = from.name,
                    description = from.description,
                    points = PointCollection(from.points),
                    personIdentifier = from.personIdentifier
            )


}