package ps.g49.socialroutingservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import ps.g49.socialroutingservice.ConnectionManager
import ps.g49.socialroutingservice.exceptions.ForbiddenRequestException
import ps.g49.socialroutingservice.models.inputModel.PersonInput
import ps.g49.socialroutingservice.mappers.outputMappers.PersonOutputMapper
import ps.g49.socialroutingservice.mappers.outputMappers.SimplifiedRouteCollectionOutputMapper
import ps.g49.socialroutingservice.models.outputModel.PersonOutput
import ps.g49.socialroutingservice.models.outputModel.SimplifiedRouteCollectionOutput
import ps.g49.socialroutingservice.models.requests.PersonRequest
import ps.g49.socialroutingservice.models.requests.PersonRoutesRequest
import ps.g49.socialroutingservice.services.PersonService
import ps.g49.socialroutingservice.utils.OutputUtils

/**
 * class responsible for the endpoints related to actions over a person
 */
@RestController
@RequestMapping("/persons")
class PersonController(
        private val personService: PersonService,
        private val connectionManager: ConnectionManager,
        private val personOutputMapper: PersonOutputMapper,
        private val simplifiedRouteCollectionOutputMapper: SimplifiedRouteCollectionOutputMapper
) {

    /**
     * endpoint used to retrieve a person's information
     * @param identifier the identifier of the person to be retrieved
     * @return an object containing the person's information
     */
    @GetMapping("/{identifier}")
    fun findUserById(@PathVariable identifier: Int): ResponseEntity<PersonOutput> {
        val person = personService.findPersonById(identifier)
        val output = personOutputMapper.map(person)
        return OutputUtils.ok(output)
    }

    /**
     * endpoint used to retrieve the routes that a user created
     * @param identifier the identifier of the owner of the routes
     * @param page the page number regarding the collection
     * @return a collection of simplified routes
     */
    @GetMapping("/{identifier}/routes")
    fun findUserCreatedRoutes(@PathVariable identifier: Int, @RequestParam page: Int = 1): ResponseEntity<SimplifiedRouteCollectionOutput> {
        val userRoutesRequest = PersonRoutesRequest.build(identifier, page)
        val simplifiedRouteCollection = personService.findUserCreatedRoutes(userRoutesRequest)
        val output = simplifiedRouteCollectionOutputMapper.map(simplifiedRouteCollection)
        return OutputUtils.ok(output)
    }

    /**
     * endpoint used to delete a person from the system
     * @param identifier the desired person to be deleted
     * @param personIdentifier the identifier associated to the authentication of the request, it must match the identifier
     */
    @DeleteMapping("/{identifier}")
    fun deletePerson(@PathVariable identifier: Int, @RequestAttribute personIdentifier: Int): ResponseEntity<Void> {
        if (identifier != personIdentifier) {
            throw ForbiddenRequestException()
        }
        personService.deletePerson(personIdentifier)
        return OutputUtils.ok()
    }

    /**
     * endpoint used to update a person's information
     * @param identifier the desired person to be deleted
     * @param personIdentifier the identifier associated to the authentication of the request, it must match the identifier
     */
    @PutMapping("/{identifier}")
    fun updatePerson(@PathVariable identifier: Int, @RequestBody personInput: PersonInput, @RequestAttribute personIdentifier: Int): ResponseEntity<Void> {
        if (identifier != personIdentifier) {
            throw ForbiddenRequestException()
        }
        val personDto = PersonRequest.build(personInput, personIdentifier)
        val connectionHandle = connectionManager.generateHandle()
        connectionHandle.use {
            personService.updatePerson(it, personDto)
            connectionHandle.close()
            return OutputUtils.ok()
        }
    }

}

