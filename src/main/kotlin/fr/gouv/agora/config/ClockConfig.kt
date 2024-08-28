package fr.gouv.agora.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.time.Clock

@Configuration
class ClockConfig {

    @Bean
    fun getClock(): Clock {
        return Clock.systemDefaultZone()
    }

}
