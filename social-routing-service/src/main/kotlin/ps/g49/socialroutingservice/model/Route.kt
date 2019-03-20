package ps.g49.socialroutingservice.model

import javax.persistence.*

@Entity
@Table(name = "Route")
data class Route(
        @Id @Column(name = "Name")
        val name: String,

        @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
        @JoinColumn(name = "RouteName", referencedColumnName = "Name", nullable = false)
        val points : Set<Point>
)