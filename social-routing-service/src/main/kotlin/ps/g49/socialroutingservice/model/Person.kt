package ps.g49.socialroutingservice.model

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Table(name = "Person")
@Entity
data class Person(
        @Column(name = "FirstName")
        val firstName: String,
        @Column(name = "LastName")
        val lastName: String,
        @Id
        @Column(name = "Email")
        val email: String? = null
)