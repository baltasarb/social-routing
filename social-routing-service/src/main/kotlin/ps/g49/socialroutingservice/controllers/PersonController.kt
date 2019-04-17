package ps.g49.socialroutingservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.inputModel.PersonInput
import ps.g49.socialroutingservice.mappers.dtoMappers.PersonDtoMapper
import ps.g49.socialroutingservice.model.Person
import ps.g49.socialroutingservice.services.PersonService

@RestController
@RequestMapping("/api.sr/persons")
class PersonController(private val personService: PersonService, private val connectionManager: ConnectionManager, val personDtoMapper: PersonDtoMapper) {

    @GetMapping("/{identifier}")
    fun findUserById(@PathVariable identifier: Int): ResponseEntity<Person> {
        val person = personService.findPersonById(identifier)
        return ResponseEntity.ok(person)
    }

    @GetMapping("/{identifier}/routes")
    fun findUserCreatedRoutes(@PathVariable identifier: Int) = personService.findUserCreatedRoutes(identifier)

    //change to request attribute if anything comes from an interceptor eg auth token
    fun createPerson(@RequestBody personInput: PersonInput) {
        val personDto = personDtoMapper.map(personInput)
        personService.createPerson(personDto)
    }

    @DeleteMapping("/{identifier}")
    fun deletePerson(@PathVariable identifier: Int) = personService.deletePerson(identifier)

    @PutMapping
    fun updatePerson(@RequestBody personInput: PersonInput) {
        val personDto = personDtoMapper.map(personInput)
        val connectionHandle = connectionManager.generateHandle()
        personService.updatePerson(connectionHandle, personDto)
        connectionHandle.close()
    }
}