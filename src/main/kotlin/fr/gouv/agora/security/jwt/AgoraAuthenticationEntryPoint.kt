package fr.gouv.agora.security.jwt

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class AgoraAuthenticationEntryPoint : AuthenticationEntryPoint {

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        println("Authentication exception: $authException")
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized")
    }

}