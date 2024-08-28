package fr.gouv.agora.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
class CrossOriginConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        val allowedOrigins = System.getenv("ALLOWED_ORIGINS").split("\n")
        registry
            .addMapping("/**")
            .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE")
            .allowedOrigins(*allowedOrigins.toTypedArray())
    }

}
