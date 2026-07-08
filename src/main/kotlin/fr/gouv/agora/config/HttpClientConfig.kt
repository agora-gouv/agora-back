package fr.gouv.agora.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.web.client.RestTemplate
import java.net.http.HttpClient
import java.time.Duration

@Configuration
class HttpClientConfig {
    @Bean
    fun httpClient(): HttpClient {
        return HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build()
    }

    @Bean
    fun restTemplate(): RestTemplate {
        val factory = SimpleClientHttpRequestFactory().apply {
            setConnectTimeout(10_000)
            setReadTimeout(30_000)
        }
        return RestTemplate(factory)
    }
}
