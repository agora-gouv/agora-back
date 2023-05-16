package fr.social.gouv.agora.config

import fr.social.gouv.agora.security.jwt.AgoraAuthenticationEntryPoint
import fr.social.gouv.agora.security.jwt.AuthenticationTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
@Suppress("unused")
class WebSecurityConfig(private val authenticationTokenFilter: AuthenticationTokenFilter) {

    @Bean
    fun authenticatedFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
            .exceptionHandling().authenticationEntryPoint(AgoraAuthenticationEntryPoint())
            .and()
            .securityMatcher("/**")
            .authorizeHttpRequests()
            .requestMatchers("/signup").permitAll()
            .requestMatchers("/login").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }

}
