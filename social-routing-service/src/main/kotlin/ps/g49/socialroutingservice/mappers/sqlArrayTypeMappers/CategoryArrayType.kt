package ps.g49.socialroutingservice.mappers.sqlArrayTypeMappers

import org.jdbi.v3.core.array.SqlArrayType
import ps.g49.socialroutingservice.models.domainModel.Category

class CategoryArrayType : SqlArrayType<Category> {
    override fun convertArrayElement(element: Category?): Any {
        return element.toString()
    }

    override fun getTypeName(): String {
        return "text"
    }
}