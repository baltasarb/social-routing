package ps.g49.socialroutingservice

import javax.persistence.*

@Table(name = "customer", schema = "t1")
@Entity
data class Customer(
        val firstName: String,
        val lastName: String,
        @Id
        val id: Long? = null
)