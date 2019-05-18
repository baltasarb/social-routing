package ps.g49.socialroutingservice.mappers.modelMappers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.Category
import ps.g49.socialroutingservice.models.domainModel.Point
import ps.g49.socialroutingservice.models.requests.RouteRequest
import ps.g49.socialroutingservice.models.domainModel.Route
import java.sql.ResultSet
import kotlin.streams.toList

@Component
class RouteMapper : ModelMapper<RouteRequest, Route> {

    override fun mapFromResultSet(rs: ResultSet): Route {
        val jsonString = buildValidJsonString(rs.getString("Points"))

        val mapper = jacksonObjectMapper()
        val pointArray = mapper.readValue(jsonString, Array<Point>::class.java)
        val pointList = pointArray.toList()

        val categoriesSQLArray : java.sql.Array = rs.getArray("Categories")
        val categoriesStringArray : Array<String> = categoriesSQLArray.array as Array<String>

        return Route(
                identifier = rs.getInt("Identifier"),
                location = rs.getString("Location"),
                name = rs.getString("Name"),
                description = rs.getString("Description"),
                rating = rs.getDouble("Rating"),
                duration = rs.getInt("Duration"),
                dateCreated = rs.getDate("DateCreated"),
                points = pointList,
                personIdentifier = rs.getInt("PersonIdentifier"),
                categories = categoriesStringArray.toList().map { Category(it) }
        )
    }


    override fun map(from: RouteRequest): Route {
        val convertedCategories = from.categories.stream().map { Category(it.name) }.toList()
        return Route(
                identifier = from.identifier,
                location = from.location,
                name = from.name,
                description = from.description,
                points = from.points,//TODO
                personIdentifier = from.personIdentifier,
                rating = from.rating,
                duration = from.duration,
                dateCreated = from.dateCreated,
                categories = convertedCategories
        )
    }



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