package ps.g49.socialroutingservice.utils.sqlQueries

import ps.g49.socialroutingservice.models.domainModel.Category

class RouteQueries {

    companion object {
        const val INSERT = "INSERT INTO Route (LocationIdentifier, Name, Description, Duration, DateCreated, Points, PersonIdentifier, ImageReference, Circular, Ordered) " +
                "VALUES (:locationIdentifier, :name, :description, :duration, CURRENT_DATE, to_json(:points), :personIdentifier, :imageReference, :circular, :ordered) " +
                "RETURNING Identifier AS route_id;"

        const val UPDATE = "UPDATE Route " +
                "SET (LocationIdentifier, Name, Description, Rating, Duration, Circular, Ordered, ImageReference, Points) = " +
                "(:locationIdentifier, :name, :description, :rating, :duration, :circular, :ordered, :imageReference, to_json(:points));"

        const val DELETE = "DELETE FROM Route WHERE identifier = ?;"

        const val SELECT_BY_ID = "SELECT Route.Identifier, Route.LocationIdentifier, Route.Name, Route.Description, Route.Rating, Route.Duration," +
                "Route.DateCreated, Route.Points, Route.PersonIdentifier, Route.Elevation, Route.Circular, Route.Ordered, Route.ImageReference, RouteCategory.CategoryName, PointOfInterest.Identifier as PointOfInterestIdentifier," +
                "PointOfInterest.Latitude, PointOfInterest.Longitude " +
                "FROM Route " +
                "JOIN RouteCategory ON RouteCategory.RouteIdentifier = Route.Identifier " +
                "JOIN RoutePointOfInterest ON RoutePointOfInterest.RouteIdentifier = Route.Identifier " +
                "JOIN PointOfInterest ON PointOfInterest.Identifier = RoutePointOfInterest.PointOfInterestIdentifier " +
                "WHERE Route.Identifier = :routeIdentifier " +
                "GROUP BY Route.Identifier, RouteCategory.CategoryName, PointOfInterest.Identifier;"

        const val SELECT_MANY = "SELECT Identifier, LocationIdentifier, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier FROM Route;"

        const val SELECT_MANY_BY_OWNER_WITH_PAGINATION_AND_COUNT = "" +
                "SELECT " +
                "COUNT(*) OVER() as count, Identifier, Name, Rating, PersonIdentifier " +
                "FROM Route " +
                "WHERE PersonIdentifier = :personIdentifier " +
                "ORDER BY Rating DESC " +
                "LIMIT :limit " +
                "OFFSET :offset;"

        const val UPDATE_ELEVATION: String = "UPDATE ROUTE " +
                "SET elevation = :elevation " +
                "WHERE Identifier = :identifier;"

        private const val SEARCH_BY_LOCATION_FIRST = "" +
                "SELECT COUNT(*) OVER() as count, Identifier, Name, Rating, PersonIdentifier " +
                "FROM Route " +
                "JOIN RouteCategory " +
                "ON RouteCategory.RouteIdentifier = Route.Identifier AND Route.Duration = :duration " +
                "WHERE LocationIdentifier = :locationIdentifier "

        private const val SEARCH_BY_LOCATION_LAST ="GROUP BY Route.identifier " +
                "ORDER BY Rating DESC " +
                "LIMIT :limit " +
                "OFFSET :offset;"

        private const val SEARCH_BY_COORDINATES_FIRST = "SELECT DISTINCT ON(Identifier) COUNT(*) OVER() as count, * " +
                "FROM (" +
                "SELECT RouteCategory.RouteIdentifier as Identifier, Route.Name, Route.Rating, Route.PersonIdentifier, Route.Points, Route.Circular, Route.Ordered FROM RouteCategory " +
                "JOIN Route ON RouteCategory.RouteIdentifier = Route.Identifier " +
                "WHERE Route.Duration = 'Short' "

        private const val SEARCH_BY_COORDINATES_LAST = "" +
                "GROUP BY RouteCategory.RouteIdentifier, Route.Name, Route.Rating, Route.PersonIdentifier, Route.Points, Route.Circular, Route.Ordered) " +
                "results " +
                "WHERE routeBelongsToUserArea(:lowerBoundRadius, :upperBoundRadius, :userLatitude, :userLongitude, results.points, results.circular, results.ordered) = TRUE " +
                "LIMIT :limit " +
                "OFFSET :offset;"

        fun getSearchByLocationQuery(categories: List<Category>): String {
            val queryStringBuilder = StringBuilder()
            queryStringBuilder.append(SEARCH_BY_LOCATION_FIRST)
            queryStringBuilder.append(dynamicallyBuildCategoryConditionalQueryParts(categories))
            queryStringBuilder.append(SEARCH_BY_LOCATION_LAST)
            return queryStringBuilder.toString()
        }

        fun getSearchByCoordinatesQuery(categories: List<Category>) : String{
            val stringBuilder = StringBuilder()
            stringBuilder.append(SEARCH_BY_COORDINATES_FIRST)
            stringBuilder.append(dynamicallyBuildCategoryConditionalQueryParts(categories))
            stringBuilder.append(SEARCH_BY_COORDINATES_LAST)
            return stringBuilder.toString()
        }

        private fun dynamicallyBuildCategoryConditionalQueryParts(categories: List<Category>) : String{
            val stringBuilder = StringBuilder()

            stringBuilder.append("AND (")
            for (i in categories.indices) {
                if (i != 0) {
                    stringBuilder.append(" OR ")
                }
                stringBuilder.append("RouteCategory.CategoryName = :category$i")
            }
            stringBuilder.append(")")

            return stringBuilder.toString()
        }

    }

}