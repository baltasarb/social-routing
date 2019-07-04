package ps.g49.socialroutingservice.utils.sqlQueries

import ps.g49.socialroutingservice.models.domainModel.Category

class RouteQueries {

    companion object {
        private const val SEARCH_BY_LOCATION = "" +
                "SELECT COUNT(*) OVER() as count, Identifier, LocationIdentifier, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier " +
                "FROM Route " +
                "JOIN RouteCategory " +
                "ON RouteCategory.RouteIdentifier = Route.Identifier " +
                "WHERE LocationIdentifier = :locationIdentifier "

        // Select Queries
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

        //Update Queries
        const val UPDATE_WITH_CATEGORIES = "" +
                "WITH UpdatedRoute AS (" +
                "UPDATE Route SET (LocationIdentifier, Name, Description, Rating, Duration, Points) = (:locationIdentifier, :name, :description, :rating, :duration, to_json(:points))" +
                "WHERE Identifier = :routeIdentifier " +
                ")" +
                "DELETE FROM RouteCategory WHERE RouteIdentifier = :routeIdentifier; " +
                "INSERT INTO RouteCategory (RouteIdentifier, CategoryName) " +
                "VALUES (" +
                ":routeIdentifier, " +
                "UNNEST(:categories)" +
                ");"

        const val UPDATE_ELEVATION: String = "UPDATE ROUTE " +
                "SET elevation = :elevation " +
                "WHERE Identifier = :identifier;"

        // Delete Queries
        const val DELETE = "DELETE FROM Route WHERE identifier = ?;"

        fun getSearchByLocationQuery(categories: List<Category>?, duration: String?) : String{
            val queryStringBuilder = StringBuilder()

            queryStringBuilder.append(SEARCH_BY_LOCATION)

            if (categories != null) {
                queryStringBuilder.append("AND (")
                for(i in categories.indices){
                    if(i != 0){
                        queryStringBuilder.append(" OR ")
                    }
                    queryStringBuilder.append("RouteCategory.CategoryName = :category$i")
                }
                queryStringBuilder.append(")")
            }

            if(duration != null){
                val durationQuery = "AND Route.Duration = :duration "
                queryStringBuilder.append(durationQuery)
            }

            queryStringBuilder.append("" +
                    "GROUP BY Route.identifier " +
                    "ORDER BY Rating DESC " +
                    "LIMIT :limit " +
                    "OFFSET :offset;"
            )
            return queryStringBuilder.toString()
        }
    }

}