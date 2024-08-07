package fr.gouv.agora.config

import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class WebConfig : WebMvcConfigurer {

    override fun configureContentNegotiation(configurer: ContentNegotiationConfigurer) {
        super.configureContentNegotiation(configurer)
        configurer
            .defaultContentType(MediaType.APPLICATION_JSON)
            .favorParameter(true)
            .ignoreAcceptHeader(true)
            .parameterName("mediaType")
            .mediaType("xml", MediaType.APPLICATION_XML)
            .mediaType("json", MediaType.APPLICATION_JSON)
    }
}
