package ps.g49.socialroutingservice.mappers.modelMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRouteWithCount
import ps.g49.socialroutingservice.models.requests.RouteRequest
import java.sql.ResultSet

@Component
class SimplifiedRouteWithCountMapper2 : ModelMapper<RouteRequest, SimplifiedRouteWithCount> {

    override fun mapFromResultSet(rs: ResultSet): SimplifiedRouteWithCount {
        return SimplifiedRouteWithCount(
                identifier = rs.getInt("Identifier"),
                name = rs.getString("Name"),
                rating = rs.getDouble("Rating"),
                personIdentifier = rs.getInt("PersonIdentifier"),
                count = rs.getInt("Count")
        )
    }

    override fun map(from: RouteRequest): SimplifiedRouteWithCount {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}