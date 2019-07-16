package ps.g49.socialroutingservice.utils.sqlQueries

class PointOfInterestQueries {

    companion object {
        const val INSERT = "INSERT INTO PointOfInterest(Identifier, Latitude, Longitude) VALUES(:identifier, :latitude, :longitude)" +
                "ON CONFLICT (Identifier) " +
                "DO NOTHING;"
    }

}