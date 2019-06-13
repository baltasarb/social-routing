package ps.g49.socialroutingservice.mappers.modelMappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.AuthenticationData
import java.sql.ResultSet

@Component
class AuthenticationDataMapper : RowMapper<AuthenticationData> {
    override fun map(rs: ResultSet?, ctx: StatementContext?): AuthenticationData {
        return AuthenticationData(
                rs!!.getLong("creationDate"),
                rs.getLong("expirationDate"),
                rs.getString("accessToken"),
                rs.getString("refreshToken"),
                rs.getInt("personIdentifier")
        )
    }
}