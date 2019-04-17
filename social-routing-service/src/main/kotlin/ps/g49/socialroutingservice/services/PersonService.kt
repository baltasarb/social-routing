package ps.g49.socialroutingservice.services

import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.inputModel.PersonInput
import ps.g49.socialroutingservice.repositories.PersonRepository
import ps.g49.socialroutingservice.repositories.RouteRepository

@Service
class PersonService(private val personRepository: PersonRepository, private val routeRepository: RouteRepository) {

    fun findPersonById(identifier: Int) = personRepository.findPersonById(identifier)

    fun findUserCreatedRoutes(identifier: Int) = routeRepository.findPersonCreatedRoutes(identifier)

    fun createPerson(personInput: PersonInput) = personRepository.create(personInput.name, personInput.email)

    fun deletePerson(identifier: Int) = personRepository.delete(identifier)

}