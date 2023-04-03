package fr.social.gouv.agora

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.server.ConfigurableWebServerFactory
import org.springframework.boot.web.server.WebServerFactoryCustomizer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.stereotype.Component
import javax.sql.DataSource


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

@Configuration
@Suppress("unused")
class JpaConfig {
    @Bean
    fun dataSource(): DataSource {
        val dataSourceBuilder = DataSourceBuilder.create()
        dataSourceBuilder.driverClassName("org.postgresql.Driver")
        dataSourceBuilder.url(System.getenv("DATABASE_URL"))
        dataSourceBuilder.username(System.getenv("DATABASE_USER"))
        dataSourceBuilder.password(System.getenv("DATABASE_PASSWORD"))
        return dataSourceBuilder.build()
    }
}