package ps.g49.socialroutingservice.utils.sqlQueries

class PersonQueries {

    companion object {
        // Select Queries
        const val SELECT = "SELECT Identifier, Rating FROM Person WHERE Identifier = ?;"

        // Insert Queries
        const val INSERT = "INSERT INTO Person (Rating) VALUES (DEFAULT);"

        // Update Queries
        const val UPDATE = "UPDATE Person SET (Rating) = (:rating) WHERE identifier = :identifier;"

        // Delete Queries
        const val DELETE = "DELETE FROM Person WHERE identifier = ?;"

    }

}