package ps.g49.socialroutingservice.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import ps.g49.socialroutingservice.services.PersonService

@RestController
@RequestMapping("/api.sr/persons")
class PersonController(private val personService: PersonService) {

    @GetMapping("/{identifier}")
    fun findUserById(@PathVariable identifier: String) = personService.findPersonById(identifier)

    @GetMapping("/{identifier}/createdRoutes")
    fun findUserCreatedRoutes(@PathVariable identifier: String) = personService.findUserCreatedRoutes(identifier)

    @GetMapping("/{identifier}/performedRoutes")
    fun findUserPerformedRoutes(@PathVariable identifier: String) = personService.findUserPerformedRoutes(identifier)

}