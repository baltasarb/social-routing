package ps.g49.socialroutingservice.services

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.models.requests.PersonRequest
import ps.g49.socialroutingservice.mappers.modelMappers.PersonMapper
import ps.g49.socialroutingservice.models.domainModel.SimplifiedRoute
import ps.g49.socialroutingservice.repositories.PersonRepository
import ps.g49.socialroutingservice.repositories.RouteRepository

@Service
class PersonService(private val personRepository: PersonRepository, private val routeRepository: RouteRepository, val personMapper: PersonMapper) {

    fun findPersonById(identifier: Int) = personRepository.findById(identifier)

    fun findUserCreatedRoutes(identifier: Int): List<SimplifiedRoute> = routeRepository.findPersonCreatedRoutes(identifier)

    fun createPerson(connectionHandle: Handle, personRequest: PersonRequest) : Int{
        val person = personMapper.map(personRequest)
        return personRepository.create(connectionHandle, person)
    }

    fun deletePerson(identifier: Int) = personRepository.delete(identifier)

    fun updatePerson(connectionHandle: Handle, personRequest: PersonRequest) {
        val person = personMapper.map(personRequest)
        personRepository.update(connectionHandle, person)
    }

}