package fr.gouv.agora.security.jwt

import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class AgoraAuthenticationEntryPoint : AuthenticationEntryPoint {
    private val logger: Logger = LoggerFactory.getLogger(AgoraAuthenticationEntryPoint::class.java)

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        authException: AuthenticationException,
    ) {
        logger.warn("Authentication exception on resource '${request.requestURI}' : $authException")
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized")
    }
}
