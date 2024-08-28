package fr.gouv.agora

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType
import io.swagger.v3.oas.annotations.info.Info
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.security.SecurityScheme
import io.swagger.v3.oas.annotations.servers.Server
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component

@SpringBootApplication(
    exclude = [
        UserDetailsServiceAutoConfiguration::class,
        RepositoryRestMvcAutoConfiguration::class
    ]
)
@OpenAPIDefinition(
    info = Info(version = "1.0", title = "Swagger Agora"),
    security = [SecurityRequirement(name = "Bearer Authentification")],
    servers = [Server(url = "/", description = "Default Server URL")],
)
@SecurityScheme(
    name = "Bearer Authentification",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT",
    description = "Header d'autorisation JWT utilisant le schéma Bearer.. \r\n\r\n Entrez votre token JWT (sans le mot Bearer) dans le champ de texte ci-dessous..\r\n\r\nExemple: \"eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJiY2ViYTEwOC01NTk0LTQ3MTYtYjIyMS1lYTE5ZjJiMjIwO\"",
)
class AgoraBackApplication

fun main(args: Array<String>) {
    FirebaseWrapper.initFirebase()
    AgoraCustomCommandHelper.storeCustomCommand(args)
    runApplication<AgoraBackApplication>(*args)
}

@Component
class ServerPortCustomizer : WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    override fun customize(factory: ConfigurableWebServerFactory) {
        val portValue = System.getenv("AGORA_PORT") ?: System.getenv("PORT")

        portValue?.toInt()?.let { port ->
            factory.setPort(port)
        }
    }
}
