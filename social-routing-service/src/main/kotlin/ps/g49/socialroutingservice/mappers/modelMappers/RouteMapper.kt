package ps.g49.socialroutingservice.mappers.modelMappers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.*
import ps.g49.socialroutingservice.models.requests.RouteRequest
import java.sql.ResultSet
import kotlin.streams.toList

@Component
class RouteMapper : ModelMapper<RouteRequest, Route> {

    override fun mapFromResultSet(rs: ResultSet): Route {
        val jsonString = buildValidJsonString(rs.getString("Points"))

        val mapper = jacksonObjectMapper()
        val pointArray = mapper.readValue(jsonString, Array<GeographicPoint>::class.java)
        val pointList = pointArray.toList()

        val categoriesSQLArray: java.sql.Array = rs.getArray("Categories")
        val categoriesStringArray: Array<String> = categoriesSQLArray.array as Array<String>

        val pointOfInterest: List<PointOfInterest>? = null//TODO

        return Route(
                identifier = rs.getInt("Identifier"),
                location = rs.getString("Location"),
                name = rs.getString("Name"),
                description = rs.getString("Description"),
                rating = rs.getDouble("Rating"),
                duration = rs.getString("Duration"),
                dateCreated = rs.getDate("DateCreated"),
                points = pointList,
                personIdentifier = rs.getInt("PersonIdentifier"),
                categories = categoriesStringArray.toList().map { Category(it) },
                elevation = rs.getDouble("Elevation"),
                isCircular = rs.getBoolean("Circular"),
                isOrdered = rs.getBoolean("Ordered"),
                pointsOfInterest = pointOfInterest!!,
                imageReference = rs.getString("Image")
        )
    }


    override fun map(from: RouteRequest): Route {
        val convertedCategories = from.categories.stream().map { Category(it.name) }.toList()

        return Route(
                identifier = from.identifier,
                location = from.location,
                name = from.name,
                description = from.description,
                points = from.geographicPoints,
                personIdentifier = from.personIdentifier,
                rating = from.rating,
                duration = from.duration,
                dateCreated = from.dateCreated,
                categories = convertedCategories,
                isOrdered = from.isOrdered,
                isCircular = from.isCircular,
                pointsOfInterest = from.pointsOfInterest,
                imageReference = from.imageReference
        )
    }

    fun buildRouteFromRedundantRouteList(redundantRouteList: List<RedundantRoute>): Route {
        val firstRoute = redundantRouteList.first()
        val categories = redundantRouteList.map { it.category }.distinct()
        val pointsOfInterest = redundantRouteList.map { it.pointOfInterest }.distinct()

        return Route(
                identifier = firstRoute.identifier,
                location = firstRoute.location,
                name = firstRoute.name,
                description = firstRoute.description,
                rating = firstRoute.rating,
                duration = firstRoute.duration,
                dateCreated = firstRoute.dateCreated,
                points = firstRoute.points,
                personIdentifier = firstRoute.personIdentifier,
                categories = categories,
                elevation = firstRoute.elevation,
                isCircular = firstRoute.isCircular,
                isOrdered = firstRoute.isOrdered,
                pointsOfInterest = pointsOfInterest,
                imageReference = firstRoute.imageReference
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
    private fun buildValidJsonString(string: String): String {
        val temp = string.filterNot { it == '\\' }
        return temp.substring(1, temp.length - 1)
    }

}