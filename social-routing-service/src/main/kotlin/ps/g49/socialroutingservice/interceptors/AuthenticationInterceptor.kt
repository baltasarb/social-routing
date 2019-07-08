package ps.g49.socialroutingservice.interceptors

import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import ps.g49.socialroutingservice.exceptions.AuthorizationHeaderException
import ps.g49.socialroutingservice.services.AuthenticationService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.web.servlet.HandlerMapping



@Component
class AuthenticationInterceptor(
        private val authenticationService: AuthenticationService
) : HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        return true
        val uri = request.requestURI
        val method = request.method

        if ((uri == "/api.sr/") or uri.contains("/google")/* or request.requestURI.contains("/error")*/)
            return true

        val token = request.getHeader("Authorization") ?: throw AuthorizationHeaderException()//todo error message

        //if the user is trying to change data the token must match its identifier
        var identifier : Int? = null
        if((method == HttpMethod.PUT.name) or (method == HttpMethod.DELETE.name)){
            val pathVariables = request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) as Map<String, Any>
            identifier = (pathVariables["Identifier"] as String).toInt()
        }

        return authenticationService.accessTokenIsValid(token) and
                if(identifier == null) true else authenticationService.userRequestIsAuthorized(identifier, token)
    }
}