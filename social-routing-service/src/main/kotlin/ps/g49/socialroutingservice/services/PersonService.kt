package ps.g49.socialroutingservice.services

import org.jdbi.v3.core.Handle
import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.models.dtos.PersonDto
import ps.g49.socialroutingservice.mappers.modelMappers.PersonMapper
import ps.g49.socialroutingservice.models.domainModel.Route
import ps.g49.socialroutingservice.repositories.PersonRepository
import ps.g49.socialroutingservice.repositories.RouteRepository

@Service
class PersonService(private val personRepository: PersonRepository, private val routeRepository: RouteRepository, val personMapper: PersonMapper) {

    fun findPersonById(identifier: Int) = personRepository.findPersonById(identifier)

    fun findUserCreatedRoutes(identifier: Int): List<Route> = routeRepository.findPersonCreatedRoutes(identifier)

    fun createPerson(connectionHandle: Handle, personDto: PersonDto) : Int{
        val person = personMapper.map(personDto)
        return personRepository.create(connectionHandle, person)
    }

    fun deletePerson(identifier: Int) = personRepository.delete(identifier)

    fun updatePerson(connectionHandle: Handle, personDto: PersonDto) {
        val person = personMapper.map(personDto)
        personRepository.update(connectionHandle, person)
    }

}