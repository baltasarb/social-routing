package ps.g49.socialroutingservice.configuration

import org.springframework.stereotype.Component
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ps.g49.socialroutingservice.interceptors.AuthenticationInterceptor
import ps.g49.socialroutingservice.interceptors.LoggingInterceptor
import ps.g49.socialroutingservice.interceptors.MediaTypeInterceptor

@Component
class ApplicationConfiguration(
        private val loggingInterceptor: LoggingInterceptor,
        private val mediaTypeInterceptor: MediaTypeInterceptor,
        private val authenticationInterceptor: AuthenticationInterceptor
) : WebMvcConfigurer {

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(loggingInterceptor)
        registry.addInterceptor(mediaTypeInterceptor)
        registry.addInterceptor(authenticationInterceptor)
        super.addInterceptors(registry)
    }

}