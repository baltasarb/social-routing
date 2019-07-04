package ps.g49.socialroutingservice.utils.sqlQueries

class RoutePointOfInterestQueries {
    companion object {
        const val INSERT = "INSERT INTO RoutePointOfInterest(RouteIdentifier, PointOfInterestIdentifier) VALUES(:routeIdentifier, :pointOfInterestIdentifier);"
        const val DELETE = "DELETE FROM RoutePointOfInterest WHERE RouteIdentifier = :routeIdentifier;"
    }
}