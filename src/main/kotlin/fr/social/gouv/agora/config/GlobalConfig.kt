package fr.social.gouv.agora.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
@Suppress("unused")
class GlobalConfig {

    @Bean
    fun getClock(): Clock {
        return Clock.systemDefaultZone()
    }

}