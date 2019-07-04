package ps.g49.socialroutingservice.utils.sqlQueries

class RouteCategoryQueries {
    companion object {
        const val INSERT = "INSERT INTO RouteCategory(RouteIdentifier, CategoryName) VALUES(:routeIdentifier, :categoryName);"

        const val DELETE = "DELETE FROM RouteCategory WHERE RouteIdentifier = :routeIdentifier;"
    }
}