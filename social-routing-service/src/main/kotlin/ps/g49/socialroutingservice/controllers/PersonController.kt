package ps.g49.socialroutingservice.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ps.g49.socialroutingservice.repositories.PersonRepository

@RestController
@RequestMapping("/api.sr/persons")
class PersonController(private val personRepository: PersonRepository) {

    @GetMapping
    fun findAll() = personRepository.findAll()

    @GetMapping("/{lastName}")
    fun findByLastName(@PathVariable lastName: String) = personRepository.findByLastName(lastName)

}