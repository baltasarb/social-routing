package ps.g49.socialroutingservice.controllers

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.inputModel.PersonInput
import ps.g49.socialroutingservice.services.PersonService

@RestController
@RequestMapping("/api.sr/persons")
class PersonController(private val personService: PersonService) {

    @GetMapping("/{identifier}")
    fun findUserById(@PathVariable identifier: Int) = personService.findPersonById(identifier)

    @GetMapping("/{identifier}/createdRoutes")
    fun findUserCreatedRoutes(@PathVariable identifier: Int) = personService.findUserCreatedRoutes(identifier)

    //change to request attribute if anything comes from an interceptor
    @PostMapping(consumes = [MediaType.APPLICATION_JSON_VALUE])
    fun createPerson(@RequestBody person : PersonInput) = personService.createPerson(person)

    @DeleteMapping("/{identifier}")
    fun deletePerson(@PathVariable identifier: Int) = personService.deletePerson(identifier)

}