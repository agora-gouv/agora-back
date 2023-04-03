package fr.social.gouv.agora

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.stereotype.Component

@SpringBootApplication
class AgoraBackApplication

fun main(args: Array<String>) {
	runApplication<AgoraBackApplication>(*args)
}

@Component
@Suppress("unused")
class ServerPortCustomizer : WebServerFactoryCustomizer<ConfigurableWebServerFactory> {
	override fun customize(factory: ConfigurableWebServerFactory) {
		System.getenv("PORT")?.toInt()?.let { port ->
			factory.setPort(port)
		}
	}
}
