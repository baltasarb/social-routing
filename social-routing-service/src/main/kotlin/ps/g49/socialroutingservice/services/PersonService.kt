package ps.g49.socialroutingservice.services

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.models.requests.PersonRequest
import ps.g49.socialroutingservice.mappers.modelMappers.PersonMapper
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRouteCollection
import ps.g49.socialroutingservice.models.requests.PersonRoutesRequest
import ps.g49.socialroutingservice.repositories.PersonRepository
import ps.g49.socialroutingservice.repositories.RouteRepository

@Service
class PersonService(private val personRepository: PersonRepository, private val routeRepository: RouteRepository, val personMapper: PersonMapper) {

    fun findPersonById(identifier: Int) = personRepository.findById(identifier)

    fun findUserCreatedRoutes(personRoutesRequest: PersonRoutesRequest): SimplifiedRouteCollection {
        return routeRepository.findPersonCreatedRoutes(personRoutesRequest.identifier, personRoutesRequest.page)
    }

    fun createPerson(connectionHandle: Handle): Int {
        return personRepository.create(connectionHandle)
    }

    fun deletePerson(identifier: Int) = personRepository.delete(identifier)

    fun updatePerson(connectionHandle: Handle, personRequest: PersonRequest) {
        val person = personMapper.map(personRequest)
        personRepository.update(connectionHandle, person)
    }

}