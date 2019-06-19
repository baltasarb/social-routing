package ps.g49.socialroutingservice.interceptors

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import ps.g49.socialroutingservice.exceptions.UnsupportedMediaTypeException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class MediaTypeInterceptor : HandlerInterceptorAdapter() {

    private val jsonContentType = "application/json"

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val method = request.method

        if (method == HttpMethod.POST.name || method == HttpMethod.PUT.name) {
            val contentType = request.getHeader(HttpHeaders.CONTENT_TYPE).toLowerCase()

            if (contentType.isEmpty() || !contentType.contains(jsonContentType))
                throw UnsupportedMediaTypeException()
        }
        return true
    }

}