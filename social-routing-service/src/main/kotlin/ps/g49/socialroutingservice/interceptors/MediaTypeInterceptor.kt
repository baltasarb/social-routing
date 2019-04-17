package ps.g49.socialroutingservice.interceptors

import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import ps.g49.socialroutingservice.exceptions.types.UnsupportedMediaTypeException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class MediaTypeInterceptor : HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        val method = request.method

        if (method == HttpMethod.POST.name || method == HttpMethod.PUT.name) {
            val contentType = request.getHeader("Content-Type")

            if (contentType.isEmpty() || contentType != "application/json")
                throw UnsupportedMediaTypeException()
        }
        return true
    }

}