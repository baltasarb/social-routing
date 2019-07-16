package ps.g49.socialroutingservice.interceptors

import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import ps.g49.socialroutingservice.exceptions.AuthorizationHeaderException
import ps.g49.socialroutingservice.services.AuthenticationService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * interceptor used to verify if the current user is authenticated correctly
 */
@Component
class AuthenticationInterceptor(
        private val authenticationService: AuthenticationService
) : HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val uri = request.requestURI

        //if the user is attempting to access the registration or root endpoints no authentication is necessary
        if ((uri == "/api.sr/") or uri.contains("/google"))
            return true

        //if the header is not present the request is invalid
        val token = request.getHeader("Authorization") ?: throw AuthorizationHeaderException()

        val personIdentifier = authenticationService.verifyTokenAndGetPersonIdentifier(token)

        //append the user's information to every request 
        request.setAttribute("personIdentifier", personIdentifier)

        return true
    }
}