package ps.g49.socialroutingservice.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ps.g49.socialroutingservice.interceptors.LoggingInterceptor

@Component
class ApplicationConfiguration (private val loggingInterceptor: LoggingInterceptor): WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loggingInterceptor)
        super.addInterceptors(registry)
    }

}