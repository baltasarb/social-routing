package ps.g49.socialroutingservice

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper
import org.springframework.stereotype.Component
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

    val jdbi: Jdbi = Jdbi.create(url, username, password) // returns a Jdbi which uses DriverManager as a connection factory.

    /**
     * @param query an sql SELECT query
     * @param mapper a mapper of the desired returning type
     * @param params the conditional parameters of the sql query, necessary if the query haves where clauses
     * @return an object of the type <R>, element found with the specified query conditions if any
     *
     * This method should be used ONLY if the user needs a SINGLE operation over a connection
     */
    fun <R> findOnly(query: String, mapper: RowMapper<R>, vararg params: String): R {
        return jdbi.withHandle<R, SQLException> { handle ->
            handle.select(query, *params)
                    .map(mapper)
                    .findOnly()
        }
    }

    fun <R> findOnlyByIntId(query: String, mapper: RowMapper<R>, id: Int): R {
        val result: Any?
        try {
            result = jdbi.withHandle<R, SQLException> { handle ->
                handle.select(query, id)
                        .map(mapper)
                        .findOnly()
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
     * This method should be used ONLY if the user needs a SINGLE operation over a connection
     */
    fun <R> findMany(query: String, mapper: RowMapper<R>, vararg params: String): List<R> {
        return jdbi.withHandle<List<R>, SQLException> { handle ->
            handle.select(query, *params)
                    .map(mapper)
                    .list()
        }
    }

    fun <R> findManyWithPagination(stringQuery: String, mapper: RowMapper<R>, page: Int, params: HashMap<String, Any>): List<R> {
        val limit = 5
        val offset = page * limit - limit
        return jdbi.withHandle<List<R>, SQLException> { handle ->
            val query = handle.select(stringQuery)
            params.forEach { (key, value) -> query.bind(key, value) }
            query.bind("limit", limit)
                    .bind("offset", offset)
                    .map(mapper)
                    .list()
        }
    }

    fun <R> findManyByIntId(query: String, mapper: RowMapper<R>, id: Int): List<R> {
        return jdbi.withHandle<List<R>, SQLException> { handle ->
            handle.select(query, id)
                    .map(mapper)
                    .list()
        }
    }

    fun <R> findManyByIntIdWithPagination(query: String, mapper: RowMapper<R>, id: Int, page : Int): List<R> {
        return jdbi.withHandle<List<R>, SQLException> { handle ->
            handle.select(query, id)
                    .map(mapper)
                    .list()
        }
    }

    fun insert(query: String, vararg params: String): Int {
        val handle = jdbi.open()
        val result = handle.execute(query, *params)
        //TODO check result
        handle.close()
        return result
    }


    fun deleteByIntId(query: String, id: Int) {
        val handle = jdbi.open()
        val result = handle.execute(query, id)
        handle.close()
    }

    fun generateHandle(): Handle = jdbi.open()

}