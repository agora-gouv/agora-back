package fr.gouv.agora.security.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.util.*
import java.util.concurrent.TimeUnit

object JwtTokenUtils {

    val JWT_TOKEN_VALIDITY = TimeUnit.DAYS.toMillis(1)
    private const val JWT_PREFIX = "Bearer "

    fun generateToken(userId: String, claims: Map<String, Any> = emptyMap()): Pair<String, Long> {
        val expirationDate = Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY)
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(Date())
            .setExpiration(expirationDate)
            .addClaims(claims)
            .signWith(getKey())
            .compact() to expirationDate.toInstant().toEpochMilli()
    }

    fun extractJwtFromHeader(authorizationHeader: String): String? {
        return authorizationHeader
            .takeIf { it.startsWith(JWT_PREFIX) }
            ?.substringAfter(JWT_PREFIX)
            ?.trim()
    }

    /**
     * Parse et vérifie le JWT une seule fois (signature + expiration), puis renvoie les claims.
     *
     * Remplace l'ancien enchaînement `isCorrectSignatureAndTokenNotExpired(jwt)` puis
     * `extractUserId(jwt)` qui parsait/validait le JWS **deux fois** par requête authentifiée.
     * Attention : un token expiré ou de signature invalide lève une `JwtException`.
     */
    fun parseClaims(jwtToken: String): Claims {
        return Jwts.parserBuilder()
            .setSigningKey(getKey())
            .build()
            .parseClaimsJws(jwtToken)
            .body
    }

    private fun getKey() = Keys.hmacShaKeyFor(Decoders.BASE64.decode(getBase64Key()))
    private fun getBase64Key() = System.getenv("JWT_SECRET") ?: ""

}
