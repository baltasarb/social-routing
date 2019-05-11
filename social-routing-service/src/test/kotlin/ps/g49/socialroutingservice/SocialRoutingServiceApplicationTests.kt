package ps.g49.socialroutingservice

import org.hamcrest.Matchers.containsString
import org.mockito.Mockito.`when`
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import ps.g49.socialroutingservice.controllers.PersonController
import ps.g49.socialroutingservice.controllers.RouteController

@RunWith(SpringRunner::class)
@SpringBootTest
@AutoConfigureMockMvc
class SocialRoutingServiceApplicationTests {

    @Autowired
    private lateinit var routeController: RouteController
    @Autowired
    private lateinit var personController: PersonController
    @Autowired
    private val mockMvc: MockMvc? = null

    @Test
    fun contextLoads() {
        assertThat(routeController).isNotNull
        assertThat(personController).isNotNull
    }

    @Test
    fun testApiRoot() {
        mockMvc!!
                .perform(get("/api.sr"))
                .andDo(print())
                .andExpect(status().isOk)
                .andExpect(content().string(containsString("")))
    }

}
