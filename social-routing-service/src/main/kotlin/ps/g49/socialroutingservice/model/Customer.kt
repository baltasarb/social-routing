package ps.g49.socialroutingservice

import javax.persistence.*

@Table(name = "Customer", schema = "t1")
@Entity
data class Customer(
        @Column(name="FirstName")
        val firstName: String,
        @Column(name="LastName")
        val lastName: String,
        @Id @Column(name="Id")
        val id: Long? = null
)