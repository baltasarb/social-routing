package ps.g49.socialroutingservice

import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.mapper.RowMapper
import org.springframework.stereotype.Component
import java.sql.SQLException

@Component
class SqlConnection {

    private val jdbi: Jdbi = Jdbi.create("jdbc:postgresql://localhost:5432/SocialRouting", "socialRoutingApi", "123")

    /**
     * @param query an sql select query
     * @param mapper a mapper of the desired returning type
     * @param params the conditional parameters of the sql query, necessary if the query haves where clauses
     * @return an object of the type <R>
     * The method returns the first element found with the specified query conditions if any
     */
    fun <R> findOnly(query: String, mapper: RowMapper<R>, vararg params: String): R {
        return jdbi.withHandle<R, SQLException> { handle ->
            handle.select(query, *params)
                    .map(mapper)
                    .findOnly()
        }
    }

    /**
     * @param query an sql select query
     * @param mapper a mapper of the desired returning type
     * @param params the conditional parameters of the sql query, necessary if the query haves where clauses     *
     * @return a list of objects of the type <R>
     * The method returns the elements found with the specified query conditions if any
     */
    fun <R> findMany(query: String, mapper: RowMapper<R>, vararg params: String): List<R> {
        return jdbi.withHandle<List<R>, SQLException> { handle ->
            handle.select(query, *params)
                    .map(mapper)
                    .list()
        }
    }

}