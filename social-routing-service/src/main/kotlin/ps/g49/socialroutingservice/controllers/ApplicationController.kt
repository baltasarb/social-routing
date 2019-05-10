package ps.g49.socialroutingservice.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import ps.g49.socialroutingservice.ApiRootResource
import ps.g49.socialroutingservice.utils.OutputUtils

@RestController
class ApplicationController {

    @GetMapping
    fun index() : ResponseEntity<ApiRootResource>{
        return OutputUtils.ok(ApiRootResource())
    }

}