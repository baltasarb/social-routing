package ps.g49.socialroutingservice.services

import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.models.domainModel.Category
import ps.g49.socialroutingservice.repositories.CategoryRepository

@Service
class CategoryService (
        private val categoryRepository: CategoryRepository
) {

    fun getAllCategories() : List<Category> = categoryRepository.findAll()

}