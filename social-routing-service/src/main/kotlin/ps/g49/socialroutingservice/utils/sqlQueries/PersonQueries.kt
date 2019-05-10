package ps.g49.socialroutingservice.utils.sqlQueries

class PersonQueries {

    companion object {
        // Select Queries
        const val SELECT = "SELECT Identifier, Name, Email, Rating FROM Person WHERE Identifier = ?;"

        // Insert Queries
        const val INSERT = "INSERT INTO Person (name, email) VALUES (:name, :email);"

        // Update Queries
        const val UPDATE = "UPDATE Person SET (Name, Email, Rating) = (:name, :email, :rating) WHERE identifier = :identifier;"

        // Delete Queries
        const val DELETE = "DELETE FROM Person WHERE identifier = ?;"
    }

}