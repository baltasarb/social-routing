package ps.g49.socialroutingservice.services

import org.springframework.stereotype.Service
import ps.g49.socialroutingservice.repositories.PersonRepository

@Service
class PersonService(private val personRepository: PersonRepository) {

    fun findPersonById(id : String) = personRepository.findPersonById(id)
}