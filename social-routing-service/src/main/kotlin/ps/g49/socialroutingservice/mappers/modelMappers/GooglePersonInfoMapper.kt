package ps.g49.socialroutingservice.mappers.modelMappers

import org.jdbi.v3.core.statement.StatementContext
import org.jdbi.v3.core.mapper.RowMapper
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.GooglePersonInfo
import java.sql.ResultSet

@Component
class GooglePersonInfoMapper : RowMapper<GooglePersonInfo> {

    override fun map(rs: ResultSet?, ctx: StatementContext?): GooglePersonInfo {
        return GooglePersonInfo(
                rs!!.getInt("PersonIdentifier"),
                rs.getString("HashedToken")
        )
    }

}