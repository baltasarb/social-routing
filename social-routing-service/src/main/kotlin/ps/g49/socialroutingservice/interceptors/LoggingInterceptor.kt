package ps.g49.socialroutingservice.interceptors

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import java.lang.Exception
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class LoggingInterceptor : HandlerInterceptorAdapter() {

    private val logger = LoggerFactory.getLogger(LoggingInterceptor::class.java)

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        super.preHandle(request, response, handler)

        logger.info("\n")

        logger.info(request.method + " " + request.requestURI)

        val headerNames = request.headerNames
        while (headerNames.hasMoreElements()) {
            val headerName = headerNames.nextElement()
            logger.info(headerName + " : " + request.getHeader(headerName))
        }
        logger.info("\n")

        //logger.info(request.)
        return true
    }

    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {
        println()
        super.postHandle(request, response, handler, modelAndView)
    }

    override fun afterCompletion(request: HttpServletRequest, response: HttpServletResponse, handler: Any, ex: Exception?) {
        println()
        super.afterCompletion(request, response, handler, ex)
    }
}