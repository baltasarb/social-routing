package ps.g49.socialroutingservice.mappers.modelMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.models.requests.RouteRequest
import java.sql.ResultSet

@Component
class SimplifiedRouteMapper : ModelMapper<RouteRequest, SimplifiedRoute> {

    override fun mapFromResultSet(rs: ResultSet): SimplifiedRoute {
        return SimplifiedRoute(
                identifier = rs.getInt("Identifier"),
                name = rs.getString("Name"),
                rating = rs.getDouble("Rating"),
                imageReference = rs.getString("ImageReference"),
                personIdentifier = rs.getInt("PersonIdentifier")
        )
    }

    override fun map(from: RouteRequest): SimplifiedRoute {
        TODO("not required")
    }

}