package fr.gouv.agora.config

import fr.gouv.agora.security.UserAuthorizationJWT
import fr.gouv.agora.security.jwt.AgoraAuthenticationEntryPoint
import fr.gouv.agora.security.jwt.AuthenticationTokenFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.ClassPathResource
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.function.RouterFunctions.resources

@Configuration
@EnableWebSecurity
@Suppress("unused")
class WebSecurityConfig(private val authenticationTokenFilter: AuthenticationTokenFilter) {

    @Bean
    fun resRouter() = resources("/**", ClassPathResource("static/"))

    @Bean
    fun authenticatedFilterChain(http: HttpSecurity): SecurityFilterChain {
        http.csrf().disable()
            .cors()
            .and()
            .exceptionHandling().authenticationEntryPoint(AgoraAuthenticationEntryPoint())
            .and()
            .securityMatcher("/**")
            .authorizeHttpRequests()
            .requestMatchers("/swagger-config.json", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
            .requestMatchers("/admin/**").hasAuthority(UserAuthorizationJWT.ADMIN_APIS.authority)
            .requestMatchers("/moderate/**").hasAuthority(UserAuthorizationJWT.MODERATE_QAG.authority)
            .requestMatchers("/signup", "/login").permitAll()
            .requestMatchers("/moderatus/**").permitAll()
            .requestMatchers("/api/public/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter::class.java)

        return http.build()
    }
}
