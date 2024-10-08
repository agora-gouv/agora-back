package fr.gouv.agora.config

import org.springframework.boot.web.servlet.FilterRegistrationBean
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.filter.ShallowEtagHeaderFilter


@Configuration
class ETagConfig {

    @Bean
    fun shallowETagHeaderFilter(): FilterRegistrationBean<ShallowEtagHeaderFilter>? {
        return FilterRegistrationBean(ShallowEtagHeaderFilter()).apply {
            addUrlPatterns("/thematiques")
            addUrlPatterns("/participation_charter")
            setName("etagFilter")
        }
    }

}
