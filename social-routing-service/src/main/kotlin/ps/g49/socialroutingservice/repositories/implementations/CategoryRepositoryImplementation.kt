package ps.g49.socialroutingservice.repositories.implementations

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.mappers.modelMappers.NewCategoryMapper
import ps.g49.socialroutingservice.models.domainModel.Category
import ps.g49.socialroutingservice.repositories.CategoryRepository

@Component
class CategoryRepositoryImplementation(
        private val connectionManager: ConnectionManager,
        private val categoryMapper: NewCategoryMapper
) : CategoryRepository {
    override fun findAll(): List<Category> {
        val query = "SELECT Name FROM Category;"
        return connectionManager.findMany(query, categoryMapper)
    }
}