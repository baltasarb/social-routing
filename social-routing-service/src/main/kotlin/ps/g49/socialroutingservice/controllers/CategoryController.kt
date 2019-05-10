package ps.g49.socialroutingservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ps.g49.socialroutingservice.models.outputModel.CategoryCollectionOutput
import ps.g49.socialroutingservice.services.CategoryService
import ps.g49.socialroutingservice.utils.OutputUtils

@RestController
@RequestMapping("/api.sr")
class CategoryController(
        private val categoryService : CategoryService
) {

    @GetMapping("/categories")
    fun getAllCategories() : ResponseEntity<CategoryCollectionOutput>{
        val categories = categoryService.getAllCategories()
        return OutputUtils.ok(CategoryCollectionOutput(categories))
    }

}