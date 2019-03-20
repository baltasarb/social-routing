package ps.g49.socialroutingservice.model

import javax.persistence.*

@Table(name = "Point")
@Entity
data class Point(
        @Column(name = "Latitude")
        val latitude: Double,

        @Id @Column(name = "Longitude")
        val longitude: Double

)