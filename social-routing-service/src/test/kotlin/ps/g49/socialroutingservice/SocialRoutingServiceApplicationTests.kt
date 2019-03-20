package ps.g49.socialroutingservice

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import ps.g49.socialroutingservice.controllers.PersonController
import ps.g49.socialroutingservice.controllers.RouteController

@RunWith(SpringRunner::class)
@SpringBootTest
class SocialRoutingServiceApplicationTests {

	@Autowired
	private lateinit var routeController: RouteController
	@Autowired
	private lateinit var personController: PersonController

	@Test
	fun contextLoads() {
		assertThat(routeController).isNotNull
		assertThat(personController).isNotNull
	}

}
