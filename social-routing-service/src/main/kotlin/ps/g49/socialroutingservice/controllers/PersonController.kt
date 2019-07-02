package ps.g49.socialroutingservice.controllers

import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.models.inputModel.PersonInput
import ps.g49.socialroutingservice.mappers.outputMappers.PersonOutputMapper
import ps.g49.socialroutingservice.mappers.outputMappers.SimplifiedRouteCollectionOutputMapper
import ps.g49.socialroutingservice.models.outputModel.PersonOutput
import ps.g49.socialroutingservice.models.outputModel.SimplifiedRouteCollectionOutput
import ps.g49.socialroutingservice.models.requests.PersonRequest
import ps.g49.socialroutingservice.models.requests.PersonRoutesRequest
import ps.g49.socialroutingservice.services.PersonService
import ps.g49.socialroutingservice.utils.OutputUtils

@RestController
@RequestMapping("/persons")
class PersonController(
        private val personService: PersonService,
        private val connectionManager: ConnectionManager,
        private val personOutputMapper: PersonOutputMapper,
        private val simplifiedRouteCollectionOutputMapper: SimplifiedRouteCollectionOutputMapper
) {

    @GetMapping("/{identifier}")
    fun findUserById(@PathVariable identifier: Int): ResponseEntity<PersonOutput> {
        val person = personService.findPersonById(identifier)
        val output = personOutputMapper.map(person)
        return OutputUtils.ok(output)
    }

    @GetMapping("/{identifier}/routes")
    fun findUserCreatedRoutes(@PathVariable identifier: Int, @RequestParam params: HashMap<String, String>): ResponseEntity<SimplifiedRouteCollectionOutput> {
        val userRoutesRequest = PersonRoutesRequest.build(identifier, params)
        val simplifiedRouteCollection = personService.findUserCreatedRoutes(userRoutesRequest)
        val output = simplifiedRouteCollectionOutputMapper.map(simplifiedRouteCollection)
        return OutputUtils.ok(output)
    }

    //change to request attribute if anything comes from an interceptor eg auth token
    @PostMapping
    fun createPerson(@RequestBody personInput: PersonInput): ResponseEntity<Void> {
        val handle = connectionManager.generateHandle()
        val id = personService.createPerson(handle)
        handle.close()

        val headers = HttpHeaders()
        headers.set("Location", OutputUtils.personUrl(id))

        return OutputUtils.ok(headers)
    }

    @DeleteMapping("/{identifier}")
    fun deletePerson(@PathVariable identifier: Int): ResponseEntity<Void> {
        personService.deletePerson(identifier)
        return OutputUtils.ok()
    }

    @PutMapping("/{identifier}")
    fun updatePerson(@PathVariable identifier: Int, @RequestBody personInput: PersonInput): ResponseEntity<Void> {
        val personDto = PersonRequest.build(personInput, identifier)
        val connectionHandle = connectionManager.generateHandle()
        personService.updatePerson(connectionHandle, personDto)
        connectionHandle.close()
        return OutputUtils.ok()
    }

}

