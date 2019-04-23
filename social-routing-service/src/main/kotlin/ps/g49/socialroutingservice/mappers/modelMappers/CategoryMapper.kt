package ps.g49.socialroutingservice.mappers.modelMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.Category
import java.sql.ResultSet

@Component
class CategoryMapper : ModelMapper<ResultSet, String> {
    override fun mapFromResultSet(rs: ResultSet): String {
        return rs.getString("CategoryName")
    }

    override fun map(from: ResultSet): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}