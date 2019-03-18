package ps.g49.socialroutingservice.repositories

import org.springframework.data.repository.CrudRepository
import ps.g49.socialroutingservice.model.Customer

interface CustomerRepository : CrudRepository<Customer, Long> {
    fun findByLastName(lastName: String): Iterable<Customer>
}