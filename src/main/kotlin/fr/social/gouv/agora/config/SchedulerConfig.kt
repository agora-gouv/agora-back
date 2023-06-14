package fr.social.gouv.agora.config

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling


@Configuration
@Suppress("unused")
@EnableScheduling
@ConditionalOnProperty(name = ["spring.scheduler.enabled"])
class SchedulerConfig