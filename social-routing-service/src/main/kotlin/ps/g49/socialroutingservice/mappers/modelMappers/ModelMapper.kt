package ps.g49.socialroutingservice.mappers.modelMappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import ps.g49.socialroutingservice.mappers.Mapper
import java.sql.ResultSet

interface ModelMapper<T, R> : Mapper<T, R>, RowMapper<R> {

    fun mapFromResultSet(rs: ResultSet): R

    /**
     * @param rs the result set received, used to map each domain object when retrieving it from the database
     * @return the domain object
     */
    override fun map(rs: ResultSet?, ctx: StatementContext?): R {
        return mapFromResultSet(rs!!)
    }

}