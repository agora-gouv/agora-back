package fr.gouv.agora.config

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
@EnableWebMvc
@Suppress("unused")
class CrossOriginConfig : WebMvcConfigurer {
    private val logger: Logger = LoggerFactory.getLogger(CrossOriginConfig::class.java)

    override fun addCorsMappings(registry: CorsRegistry) {
        val allowedOrigins = System.getenv("ALLOWED_ORIGINS").split("\n")
        logger.debug("allowedOrigins = {}", allowedOrigins)
        registry
            .addMapping("/**")
            .allowedMethods("GET", "HEAD", "POST", "PUT", "DELETE")
            .allowedOrigins(*allowedOrigins.toTypedArray())
    }

}
