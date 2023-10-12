package fr.social.gouv.agora.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.util.*
import java.util.concurrent.TimeUnit


object JwtTokenUtils {

    private val JWT_TOKEN_VALIDITY = TimeUnit.DAYS.toMillis(1)
    private const val JWT_PREFIX = "Bearer "

    private val SHORT_JWT_TOKEN_VALIDITY = TimeUnit.MINUTES.toMillis(2)
    private const val SHORT_JWT_TOKEN_USER_ID = "b6953c78-dec3-4fa6-8d9f-18dd44271874"

    fun generateToken(userId: String, claims: Map<String, Any> = emptyMap()): String {
        if (userId == SHORT_JWT_TOKEN_USER_ID) {
            return Jwts.builder()
                .setSubject(userId)
                .setIssuedAt(Date())
                .setExpiration(Date(System.currentTimeMillis() + SHORT_JWT_TOKEN_VALIDITY))
                .addClaims(claims)
                .signWith(getKey())
                .compact()
        }
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY))
            .addClaims(claims)
            .signWith(getKey())
            .compact()
    }

    fun extractUserId(jwtToken: String): String {
        return extractClaim(jwtToken) { claims -> claims.subject }
    }

    fun extractJwtFromHeader(authorizationHeader: String): String? {
        return authorizationHeader
            .takeIf { it.startsWith(JWT_PREFIX) }
            ?.substringAfter(JWT_PREFIX)
            ?.trim()
    }

    fun extractUserIdFromHeader(authorizationHeader: String): String {
        return extractUserId(extractJwtFromHeader(authorizationHeader) ?: "")
    }

    fun isCorrectSignatureAndTokenNotExpired(jwtToken: String): Boolean {
        val expiration = extractExpirationDate(jwtToken)
        return expiration.after(Date())
    }

    private fun extractExpirationDate(jwtToken: String): Date {
        return extractClaim(jwtToken) { claims -> claims.expiration }
    }

    private fun <T> extractClaim(jwtToken: String, claimsResolver: (Claims) -> T): T {
        val claims = extractAllClaims(jwtToken)
        return claimsResolver.invoke(claims)
    }

    private fun extractAllClaims(jwtToken: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(jwtToken)
            .body
    }

    private fun getKey() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(getBase64Key()))
    private fun getBase64Key() = System.getenv("JWT_SECRET") ?: ""

}