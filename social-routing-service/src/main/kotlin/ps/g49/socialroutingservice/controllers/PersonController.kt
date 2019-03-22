package ps.g49.socialroutingservice.controllers

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api.sr/persons")
class PersonController {

    @GetMapping
    fun findUser() = "User found."

}