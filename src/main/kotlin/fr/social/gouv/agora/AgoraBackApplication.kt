package fr.social.gouv.agora

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component

@SpringBootApplication(exclude = [UserDetailsServiceAutoConfiguration::class])
class AgoraBackApplication

fun main(args: Array<String>) {
    FirebaseWrapper.initFirebase()
    AgoraCustomCommandHelper.storeCustomCommand(args)
    runApplication<AgoraBackApplication>(*args)
}

@Component
@Suppress("unused")
class ServerPortCustomizer : WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
    override fun customize(factory: ConfigurableWebServerFactory) {
        val portValue = System.getenv("AGORA_PORT") ?: System.getenv("PORT")

        portValue?.toInt()?.let { port ->
            factory.setPort(port)
        }
    }
}
