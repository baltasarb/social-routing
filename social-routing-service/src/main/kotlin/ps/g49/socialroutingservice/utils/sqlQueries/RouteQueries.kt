package ps.g49.socialroutingservice.utils.sqlQueries

class RouteQueries {

    companion object {
        // Select Queries
        const val SELECT = "SELECT Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier FROM Route WHERE Identifier = ?;"
        const val SELECT_ROUTE_CATEGORIES = "SELECT CategoryName FROM RouteCategory WHERE RouteIdentifier = ?;"
        const val SELECT_MANY = "SELECT Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier FROM Route;"
        const val SELECT_MANY_BY_LOCATION = "SELECT Identifier, Location, Name, Description, Rating, Duration, DateCreated, Points, PersonIdentifier FROM Route WHERE Location = ?;"
        const val SELECT_MANY_BY_OWNER = "SELECT Identifier, Name, Rating, PersonIdentifier FROM Route WHERE PersonIdentifier = ?;"

        // Insert Queries
        const val INSERT_WITH_CATEGORIES = "WITH InsertedRoute AS (" +
                "INSERT INTO Route (Location, Name, Description, Duration, DateCreated, Points, PersonIdentifier) " +
                "VALUES (:location, :name, :description, :duration, CURRENT_DATE, to_json(:points), :personIdentifier) " +
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
                "UPDATE Route SET (Location, Name, Description, Rating, Duration, Points) = (:location, :name, :description, :rating, :duration, to_json(:points))" +
                "WHERE Identifier = :routeIdentifier " +
                ")" +
                "DELETE FROM RouteCategory WHERE RouteIdentifier = :routeIdentifier; " +
                "INSERT INTO RouteCategory (RouteIdentifier, CategoryName) " +
                "VALUES (" +
                ":routeIdentifier, " +
                "UNNEST(:categories)" +
                ");"

        // Delete Queries
        const val DELETE = "DELETE FROM Route WHERE identifier = ?;"
        const val DELETE_ROUTE_CATEGORIES = "DELETE FROM RouteCategory WHERE RouteIdentifier = :identifier;"
    }

}