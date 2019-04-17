package ps.g49.socialroutingservice.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.inputModel.PersonInput
import ps.g49.socialroutingservice.model.Person
import ps.g49.socialroutingservice.services.PersonService

@RestController
@RequestMapping("/api.sr/persons")
class PersonController(private val personService: PersonService) {

    @GetMapping("/{identifier}")
    fun findUserById(@PathVariable identifier: Int) : ResponseEntity<Person> {
        val person = personService.findPersonById(identifier)

        return ResponseEntity(person, HttpStatus.OK)
    }

    @GetMapping("/{identifier}/routes")
    fun findUserCreatedRoutes(@PathVariable identifier: Int) = personService.findUserCreatedRoutes(identifier)

    //change to request attribute if anything comes from an interceptor eg auth token
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createPerson(@RequestBody person : PersonInput) = personService.createPerson(person)

    @DeleteMapping("/{identifier}")
    fun deletePerson(@PathVariable identifier: Int) = personService.deletePerson(identifier)

}