package ps.g49.socialroutingservice

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.exceptions.InternalServerErrorException
import ps.g49.socialroutingservice.exceptions.ResourceNotFoundException
import java.sql.SQLException

@Component
class ConnectionManager {

    private val host = "localhost"
    private val port = "5432"
    private val databaseName = "SocialRouting"
    private val username = "socialRoutingApi"
    private val password = "123"
    private val url = "jdbc:postgresql://localhost:5432/SocialRouting"


    private val db = "SocialRoutingDatabase"
    private val newUrl = "jdbc:postgresql://35.246.58.159/$db"
    private val newUsername = "socialRoutingApi"
    private val newPassword = "nAGqC9G7wRUwrwt"

    private val jdbi: Jdbi = Jdbi.create(url, username, password) // returns a Jdbi instance which uses DriverManager as a connection factory.

    fun generateHandle(): Handle = jdbi.open()

    /**
     * @param query an sql SELECT query
     * @param mapper a mapper of the desired returning type
     * @param params the conditional parameters of the sql query, necessary if the query haves where clauses
     * @return an object of the type <R>, element found with the specified query conditions if any
     *
     * This method should be used ONLY if the user needs a SINGLE operation over a connection
     */
    fun <R> findOnlyByParams(query: String, mapper: RowMapper<R>, params: HashMap<String, Any>): R {
        val result: Any?
        try {
            result = jdbi.withHandle<R, SQLException> { handle ->
                val statement = handle.select(query)
                params.forEach { (key, value) -> statement.bind(key, value) }
                statement.map(mapper).findOnly()
            }
        } catch (exception: IllegalStateException) {
            throw ResourceNotFoundException()
        }
        return result
    }

    /**
     * @param query an sql SELECT query
     * @param mapper a mapper of the desired returning type
     * @param params the conditional parameters of the sql query, necessary if the query haves where clauses     *
     * @return a list of objects of the type <R>, elements found with the specified query conditions if any
     *
     * This method should be used ONLY if the user needs a SINGLE operation over a connection, self closable
     */
    fun <R> findMany(query: String, mapper: RowMapper<R>, vararg params: Any): List<R> {
        val result: Any?
        try {
            result = jdbi.withHandle<List<R>, SQLException> { handle ->
                handle.select(query, *params)
                        .map(mapper)
                        .list()
            }
        } catch (illegalStateException: IllegalStateException) {
            throw ResourceNotFoundException()
        } catch (exception: Exception) {
            throw InternalServerErrorException()
        }
        return result
    }

    /**
     * @param query an sql SELECT query
     * @param mapper a mapper of the desired returning type
     * @param page the desired page of the collection
     * @param params the conditional parameters of the sql query, necessary if the query haves where clauses     *
     * @return a list of objects of the type <R>, elements found with the specified query conditions if any
     *
     * This method should be used ONLY if the user needs a SINGLE operation over a connection, self closable
     */
    fun <R> findManyWithPagination(limit: Int, query: String, mapper: RowMapper<R>, page: Int, params: HashMap<String, Any>): List<R> {
        val offset = page * limit - limit
        val result: Any?
        try {
            result = jdbi.withHandle<List<R>, SQLException> { handle ->
                val statement = handle.select(query)
                params.forEach { (key, value) -> statement.bind(key, value) }
                statement.bind("limit", limit)
                        .bind("offset", offset)
                        .map(mapper)
                        .list()
            }
        } catch (illegalStateException: IllegalStateException) {
            throw ResourceNotFoundException()
        } catch (exception: Exception) {
            throw InternalServerErrorException()
        }
        return result
    }

    fun deleteByIntId(query: String, id: Int) {
        try {
            jdbi.open().use {
                it.execute(query, id)
            }
        } catch (e: Exception) {
            throw InternalServerErrorException()
        }
    }

}