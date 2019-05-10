package ps.g49.socialroutingservice.repositories

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.Category

interface CategoryRepository {

    fun findAll(): List<Category>

}