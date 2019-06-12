package ps.g49.socialroutingservice.mappers.modelMappers

import org.jdbi.v3.core.statement.StatementContext
import org.jdbi.v3.core.mapper.RowMapper
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.GoogleAuthenticationData
import java.sql.ResultSet

@Component
class GooglePersonInfoMapper : RowMapper<GoogleAuthenticationData> {

    override fun map(rs: ResultSet?, ctx: StatementContext?): GoogleAuthenticationData {
        return GoogleAuthenticationData(
                rs!!.getString("Subject")
        )
    }

}