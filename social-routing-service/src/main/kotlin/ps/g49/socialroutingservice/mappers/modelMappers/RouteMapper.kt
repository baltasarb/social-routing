package ps.g49.socialroutingservice.mappers.modelMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.dtos.RouteDto
import ps.g49.socialroutingservice.models.domainModel.Route
import java.sql.ResultSet

@Component
class RouteMapper : ModelMapper<RouteDto, Route> {

    override fun mapFromResultSet(rs: ResultSet): Route = Route(
            identifier = rs.getInt("Identifier"),
            location = rs.getString("Location"),
            name = rs.getString("Name"),
            description = rs.getString("Description"),
            rating = rs.getDouble("Rating"),
            duration = rs.getInt("Duration"),
            dateCreated = rs.getDate("DateCreated"),
            points = listOf(),//TODO
            personIdentifier = rs.getInt("PersonIdentifier")
    )

    override fun map(from: RouteDto): Route =
            Route(
                    identifier = from.identifier,
                    location = from.location,
                    name = from.name,
                    description = from.description,
                    points = listOf(),//TODO
                    personIdentifier = from.personIdentifier,
                    rating = from.rating,
                    duration = from.duration,
                    dateCreated = from.dateCreated
            )

}