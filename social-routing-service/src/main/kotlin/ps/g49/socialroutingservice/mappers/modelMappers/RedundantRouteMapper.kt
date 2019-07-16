package ps.g49.socialroutingservice.mappers.modelMappers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.Category
import ps.g49.socialroutingservice.models.domainModel.GeographicPoint
import ps.g49.socialroutingservice.models.domainModel.PointOfInterest
import ps.g49.socialroutingservice.models.domainModel.RedundantRoute
import java.sql.ResultSet

@Component
class RedundantRouteMapper : RowMapper<RedundantRoute> {
    override fun map(rs: ResultSet?, ctx: StatementContext?): RedundantRoute {

        val jsonString = buildValidJsonString(rs!!.getString("Points"))

        val mapper = jacksonObjectMapper()
        val pointArray = mapper.readValue(jsonString, Array<GeographicPoint>::class.java)
        val pointList = pointArray.toList()

        val poiLatitude = rs.getDouble("Latitude")
        val poiLongitude = rs.getDouble("Longitude")
        val poiIdentifier = rs.getString("PointOfInterestIdentifier")
        val pointOfInterest = PointOfInterest(poiIdentifier, poiLatitude,poiLongitude)

        val category = Category(rs.getString("CategoryName"))

        return RedundantRoute(
                identifier = rs.getInt("Identifier"),
                location = rs.getString("LocationIdentifier"),
                name = rs.getString("Name"),
                description = rs.getString("Description"),
                rating = rs.getDouble("Rating"),
                duration = rs.getString("Duration"),
                dateCreated = rs.getDate("DateCreated"),
                points = pointList,
                personIdentifier = rs.getInt("PersonIdentifier"),
                category = category,
                elevation = rs.getDouble("Elevation"),
                isCircular = rs.getBoolean("Circular"),
                isOrdered = rs.getBoolean("Ordered"),
                pointOfInterest = pointOfInterest,
                imageReference = rs.getString("ImageReference")
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