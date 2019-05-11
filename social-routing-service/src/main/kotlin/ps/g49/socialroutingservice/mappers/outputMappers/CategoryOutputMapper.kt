package ps.g49.socialroutingservice.mappers.outputMappers

import org.springframework.stereotype.Component
import ps.g49.socialroutingservice.models.domainModel.Category
import ps.g49.socialroutingservice.models.outputModel.CategoryOutputCollection
import ps.g49.socialroutingservice.models.outputModel.CategoryOutput

@Component
class CategoryOutputMapper : OutputMapper<Category, CategoryOutput>, OutputCollectionMapper<Category, CategoryOutputCollection> {

    override fun mapCollection(list: List<Category>): CategoryOutputCollection {
        return CategoryOutputCollection(list.map { map(it) })
    }

    override fun map(from: Category): CategoryOutput {
        return CategoryOutput(from.name)
    }
}