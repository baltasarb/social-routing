package ps.g49.socialroutingservice.mappers.modelMappers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.Point
import ps.g49.socialroutingservice.models.dtos.RouteDto
import ps.g49.socialroutingservice.models.domainModel.Route
import java.sql.ResultSet

@Component
class RouteMapper : ModelMapper<RouteDto, Route> {

    override fun mapFromResultSet(rs: ResultSet): Route {
        val s = rs.getString("Points")
        val jsonString = buildValidJsonString(rs.getString("Points"))

        val mapper = jacksonObjectMapper()
        val pointArray = mapper.readValue(jsonString, Array<Point>::class.java)
        val pointList = pointArray.toList()

        return Route(
                identifier = rs.getInt("Identifier"),
                location = rs.getString("Location"),
                name = rs.getString("Name"),
                description = rs.getString("Description"),
                rating = rs.getDouble("Rating"),
                duration = rs.getInt("Duration"),
                dateCreated = rs.getDate("DateCreated"),
                points = pointList,//TODO
                personIdentifier = rs.getInt("PersonIdentifier")
        )
    }


    override fun map(from: RouteDto): Route =
            Route(
                    identifier = from.identifier,
                    location = from.location,
                    name = from.name,
                    description = from.description,
                    points = from.points,//TODO
                    personIdentifier = from.personIdentifier,
                    rating = from.rating,
                    duration = from.duration,
                    dateCreated = from.dateCreated,
                    categories = from.categories
            )


    /**
     * the result set returns the json object with \ escape characters that need o be removed
     * it also adds an extra pair of " characters. They are removed by using the substring of the json
     *
     * this method builds valid jason from a string with the following format:
     *  "[{\"latitude\":3.0,\"longitude\":4.0},{\"latitude\":3.0,\"longitude\":4.0}]"
     *  to:
     *  [{"latitude":3.0,"longitude":4.0},{"latitude":3.0,"longitude":4.0}]
     */
    private fun buildValidJsonString(string: String) : String{
        val temp = string.filterNot { it == '\\' }
        return temp.substring(1, temp.length - 1)
    }

}