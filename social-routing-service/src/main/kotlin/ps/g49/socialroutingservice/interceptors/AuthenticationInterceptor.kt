package ps.g49.socialroutingservice.interceptors

import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import ps.g49.socialroutingservice.exceptions.AuthorizationHeaderException
import ps.g49.socialroutingservice.services.AuthenticationService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Component
class AuthenticationInterceptor(
        private val authenticationService: AuthenticationService
) : HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val uri = request.requestURI

        if ((uri == "/api.sr/") or uri.contains("/google"))
            return true

        val token = request.getHeader("Authorization") ?: throw AuthorizationHeaderException()

        val personIdentifier = authenticationService.verifyTokenAndGetPersonIdentifier(token)

        request.setAttribute("personIdentifier", personIdentifier)

        return true
    }
}