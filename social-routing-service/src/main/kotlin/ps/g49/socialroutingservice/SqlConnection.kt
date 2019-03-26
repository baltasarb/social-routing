package ps.g49.socialroutingservice

import org.jdbi.v3.core.Jdbi
import org.springframework.stereotype.Component

@Component
class SqlConnection {

    val jdbi: Jdbi = Jdbi.create("jdbc:postgresql://localhost:5432/SocialRouting", "baltasarb", "baltasarb")


}