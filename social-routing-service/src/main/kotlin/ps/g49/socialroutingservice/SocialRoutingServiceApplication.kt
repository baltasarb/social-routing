package ps.g49.socialroutingservice

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import ps.g49.socialroutingservice.model.Person
import ps.g49.socialroutingservice.repositories.PersonRepository

@SpringBootApplication
class SocialRoutingServiceApplication {

    private val log = LoggerFactory.getLogger(SocialRoutingServiceApplication::class.java)

  /*  @Bean
    fun initPersonTest(repository: CustomerRepository) = CommandLineRunner {
        // save a couple of customers
        repository.save(Customer("Jack", "Bauer",1L))
        repository.save(Customer("Chloe", "O'Brian", 2L))
        repository.save(Customer("Kim", "Bauer", 3L))
        repository.save(Customer("David", "Palmer", 4L))
        repository.save(Customer("Michelle", "Dessler", 5L))

        // fetch all customers
        log.info("Customers found with findAll():")
        log.info("-------------------------------")
        repository.findAll().forEach { log.info(it.toString()) }
        log.info("")

        // fetch an individual customer by ID
        val customer = repository.findById(1L)
        customer.ifPresent {
            log.info("Customer found with findById(1L):")
            log.info("--------------------------------")
            log.info(it.toString())
            log.info("")
        }

        // fetch customers by last name
        log.info("Customer found with findByLastName('Bauer'):")
        log.info("--------------------------------------------")
        repository.findByLastName("Bauer").forEach { log.info(it.toString()) }
        log.info("")
    }*/


    @Bean
    fun initPersonTest(repository: PersonRepository) = CommandLineRunner {
        // save a couple of customers
        repository.save(Person("Jack", "Bauer", "email 1"))
        repository.save(Person("Chloe", "O'Brian", "email 2"))
        repository.save(Person("Kim", "Bauer", "email 3"))
        repository.save(Person("David", "Palmer", "email 4"))
        repository.save(Person("Michelle", "Dessler", "email 5"))

        // fetch all customers
        log.info("Customers found with findAll():")
        log.info("-------------------------------")
        repository.findAll().forEach { log.info(it.toString()) }
        log.info("")

        // fetch an individual customer by ID
        val customer = repository.findById("email 2")
        customer.ifPresent {
            log.info("Customer found with findById(email 2):")
            log.info("--------------------------------")
            log.info(it.toString())
            log.info("")
        }

        // fetch customers by last name
        log.info("Customer found with findByLastName('Bauer'):")
        log.info("--------------------------------------------")
        repository.findByLastName("Bauer").forEach { log.info(it.toString()) }
        log.info("")
    }
}

fun main(args: Array<String>) {
    runApplication<SocialRoutingServiceApplication>(*args)
}
