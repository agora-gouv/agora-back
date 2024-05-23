package fr.gouv.agora.security.jwt

import fr.gouv.agora.security.UserJwtMapper
import fr.gouv.agora.usecase.login.LoginUseCase
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthenticationTokenFilter(
    private val loginUseCase: LoginUseCase,
    private val userJwtMapper: UserJwtMapper,
) : OncePerRequestFilter() {
    private val loggerSl4j: Logger = LoggerFactory.getLogger(AuthenticationTokenFilter::class.java)

    companion object {
        private const val JWT_HEADER_KEY = "Authorization"
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain,
    ) {
        extractJwt(request)?.let { jwtToken ->
            try {
                if (JwtTokenUtils.isCorrectSignatureAndTokenNotExpired(jwtToken)) {
                    loginWithUserId(JwtTokenUtils.extractUserId(jwtToken))
                }
            } catch (e: JwtException) {
                loggerSl4j.error("JwtException: $e")
            }
        }

        filterChain.doFilter(request, response)
    }

    private fun extractJwt(request: HttpServletRequest): String? {
        return request.getHeader(JWT_HEADER_KEY)?.let(JwtTokenUtils::extractJwtFromHeader)
    }

    private fun loginWithUserId(userId: String) {
        loginUseCase.findUser(userId)?.let { userInfo ->
            val userDetails = userJwtMapper.toJwt(userInfo)
            SecurityContextHolder.getContext().authentication = UsernamePasswordAuthenticationToken(
                /* principal */
                userDetails,
                /* credentials */
                null,
                /* authorities */
                userDetails.authorizationList,
            )
        }
    }

}
