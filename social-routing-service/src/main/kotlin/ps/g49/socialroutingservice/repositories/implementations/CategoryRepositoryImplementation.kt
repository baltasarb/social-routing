package ps.g49.socialroutingservice.repositories.implementations

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.mappers.modelMappers.CategoryMapper
import ps.g49.socialroutingservice.models.domainModel.Category
import ps.g49.socialroutingservice.repositories.CategoryRepository
import ps.g49.socialroutingservice.utils.sqlQueries.CategoryQueries

@Component
class CategoryRepositoryImplementation(
        private val connectionManager: ConnectionManager,
        private val categoryMapper: CategoryMapper
) : CategoryRepository {
    override fun findAll(): List<Category> {
        return connectionManager.findMany(CategoryQueries.SELECT_MANY, categoryMapper)
    }
}