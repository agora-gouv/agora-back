package fr.gouv.agora

import io.swagger.v3.oas.models.OpenAPI
import org.springdoc.data.rest.SpringDocDataRestConfiguration
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer


//@Configuration
//@EnableWebMvc
//@ComponentScan(basePackages = ["org.springdoc"])
//@EnableAutoConfiguration(exclude = [SpringDocDataRestConfiguration::class])
//@PropertySource("classpath:/application.properties")
//class SwaggerConfiguration : WebMvcConfigurer {
//    @Bean
//    fun openAPI(): OpenAPI {
//        return OpenAPI()
//    }
//}
