package ps.g49.socialroutingservice.mappers.modelMappers

import org.jdbi.v3.core.mapper.RowMapper
import org.jdbi.v3.core.statement.StatementContext
import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.Category
import java.sql.ResultSet

@Component
class NewCategoryMapper : RowMapper<Category> {
    override fun map(rs: ResultSet?, ctx: StatementContext?): Category {
        return Category(rs!!.getString("Name"))
    }
}