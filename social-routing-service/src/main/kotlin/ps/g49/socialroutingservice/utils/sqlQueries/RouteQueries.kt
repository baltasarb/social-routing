package ps.g49.socialroutingservice.utils.sqlQueries

class RouteQueries {

    companion object {
        const val SEARCH_BY_LOCATION = "" +
                "SELECT COUNT(*) OVER() as count, Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier " +
                "FROM Route " +
                "JOIN RouteCategory " +
                "ON RouteCategory.RouteIdentifier = Route.Identifier " +
                "WHERE Location = :location "


        // Select Queries
        const val SELECT_BY_ID_WITH_CATEGORIES = "" +
                "SELECT Route.Identifier, Route.Location, Route.Name, Route.Description, Route.Rating, Route.Duration, Route.DateCreated, Route.Points, Route.PersonIdentifier, Route.Elevation, array_agg(RouteCategory.CategoryName) AS Categories " +
                "FROM Route " +
                "JOIN RouteCategory " +
                "ON RouteCategory.RouteIdentifier = Route.Identifier " +
                "WHERE Identifier = :routeIdentifier " +
                "GROUP BY Route.Identifier;"

        const val SELECT_MANY = "SELECT Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier FROM Route;"

        const val SELECT_MANY_BY_LOCATION_WITH_PAGINATION = "" +
                "SELECT COUNT(*) OVER() as count, Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier " +
                "FROM Route " +
                "WHERE Location = :location " +
                "ORDER BY Rating DESC " +
                "LIMIT :limit " +
                "OFFSET :offset;"

        const val SELECT_MANY_BY_OWNER_WITH_PAGINATION_AND_COUNT = "" +
                "SELECT " +
                "COUNT(*) OVER() as count, Identifier, Name, Rating, PersonIdentifier " +
                "FROM Route " +
                "WHERE PersonIdentifier = :personIdentifier " +
                "ORDER BY Rating DESC " +
                "LIMIT :limit " +
                "OFFSET :offset;"

        // Insert Queries
        const val INSERT_WITH_CATEGORIES = "WITH InsertedRoute AS (" +
                "INSERT INTO Route (Location, Name, Description, Duration, DateCreated, Points, PersonIdentifier) " +
                "VALUES (:location, :name, :description, :duration, CURRENT_DATE, to_json(:geographicPoints), :personIdentifier) " +
                "RETURNING Identifier AS route_id" +
                ") " +
                "INSERT INTO RouteCategory (RouteIdentifier, CategoryName) " +
                "VALUES (" +
                "(SELECT route_id FROM InsertedRoute), " +
                "UNNEST(:categories)" +
                ") " +
                "RETURNING (SELECT route_id FROM InsertedRoute);"

        //Update Queries
        const val UPDATE_WITH_CATEGORIES = "" +
                "WITH UpdatedRoute AS (" +
                "UPDATE Route SET (Location, Name, Description, Rating, Duration, Points) = (:location, :name, :description, :rating, :duration, to_json(:geographicPoints))" +
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
    }

}