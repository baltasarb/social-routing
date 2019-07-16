package ps.g49.socialroutingservice.utils.sqlQueries

class ImageQueries {

    companion object {
        const val INSERT = "INSERT INTO Image (Reference) VALUES(:reference)" +
                "ON CONFLICT (Reference)" +
                "DO NOTHING;"
    }

}