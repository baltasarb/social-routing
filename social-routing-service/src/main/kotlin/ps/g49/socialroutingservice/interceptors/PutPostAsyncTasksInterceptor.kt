package ps.g49.socialroutingservice.interceptors

import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.servlet.ModelAndView
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import ps.g49.socialroutingservice.services.RouteElevationAsyncService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class PutPostAsyncTasksInterceptor (private val routeElevationService : RouteElevationAsyncService): HandlerInterceptorAdapter() {
    override fun postHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any, modelAndView: ModelAndView?) {

        val requestMethod = request.method
        val statusCode = response.status

        if((requestMethod == HttpMethod.PUT.name) or (requestMethod == HttpMethod.POST.name)){
            if((statusCode == HttpStatus.CREATED.value()) or (statusCode == HttpStatus.OK.value())){


                //routeElevationService.findElevation()
            }
        }

    }
}