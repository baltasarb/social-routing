package ps.g49.socialroutingservice.mappers.modelMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRouteWithCount
import ps.g49.socialroutingservice.models.requests.RouteRequest
import java.sql.ResultSet

@Component
class SimplifiedRouteWithCountMapper : ModelMapper<RouteRequest, SimplifiedRouteWithCount> {

    override fun mapFromResultSet(rs: ResultSet): SimplifiedRouteWithCount {
        return SimplifiedRouteWithCount(
                identifier = rs.getInt("Identifier"),
                name = rs.getString("Name"),
                rating = rs.getDouble("Rating"),
                imageReference = rs.getString("ImageReference"),
                personIdentifier = rs.getInt("PersonIdentifier"),
                count = rs.getInt("Count")
        )
    }

    override fun map(from: RouteRequest): SimplifiedRouteWithCount {
        TODO("not required")
    }

}