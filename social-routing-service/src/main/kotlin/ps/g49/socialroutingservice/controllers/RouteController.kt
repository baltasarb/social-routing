package ps.g49.socialroutingservice.controllers

import org.springframework.web.bind.annotation.*
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.statement.StatementContext
import java.lang.RuntimeException
import java.sql.ResultSet


@RestController
@RequestMapping("/api.sr/routes")
class RouteController {

    @GetMapping
    fun findAllRoutes() : String{

        val jdbi = Jdbi.create("jdbc:postgresql://localhost:5432/SocialRouting", "baltasarb", "baltasarb")

        val routeName : String = jdbi.withHandle<String, RuntimeException> { handle ->
            handle.createQuery("SELECT Name FROM Route WHERE Name = 'testRoute';")
                    .mapTo(String::class.java)//when a query returns a single column, we can map it to the desired Java type:
                    .findOnly()//for a single result
        }

        return routeName
    }

}