package ps.g49.socialroutingservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ps.g49.socialroutingservice.mappers.outputMappers.CategoryOutputMapper
import ps.g49.socialroutingservice.models.outputModel.CategoryOutputCollection
import ps.g49.socialroutingservice.services.CategoryService
import ps.g49.socialroutingservice.utils.OutputUtils

@RestController
class CategoryController(
        private val categoryService : CategoryService,
        private val categoryOutputMapper: CategoryOutputMapper
) {

    @GetMapping("/categories")
    fun getAllCategories() : ResponseEntity<CategoryOutputCollection>{
        val categories = categoryService.getAllCategories()
        val collection = categoryOutputMapper.mapCollection(categories)
        return OutputUtils.ok(collection)
    }

}