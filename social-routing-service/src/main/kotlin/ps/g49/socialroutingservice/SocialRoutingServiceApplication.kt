package ps.g49.socialroutingservice

import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import ps.g49.socialroutingservice.repositories.PersonRepository

@SpringBootApplication
class SocialRoutingServiceApplication {

    private val log = LoggerFactory.getLogger(SocialRoutingServiceApplication::class.java)

    @Bean
    fun initTests() = CommandLineRunner {
        log.info("Initialization Test Started.")
       //todo when testing is required
        log.info("Initialization Test Finished.")
    }

}

fun main(args: Array<String>) {
    runApplication<SocialRoutingServiceApplication>(*args)
}
