package fr.gouv.agora.security.jwt

import io.jsonwebtoken.JwtException
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Assumptions.assumeTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class JwtTokenUtilsTest {

    @BeforeEach
    fun setUp() {
        // Le secret est fourni par la tâche Gradle `test` (environment JWT_SECRET).
        assumeTrue(System.getenv("JWT_SECRET")?.isNotBlank() == true) { "JWT_SECRET non défini" }
    }

    @Test
    fun `parseClaims - returns the userId (subject) of a freshly generated token`() {
        // Given
        val userId = "11111111-1111-1111-1111-111111111111"
        val (token, _) = JwtTokenUtils.generateToken(userId)

        // When
        val claims = JwtTokenUtils.parseClaims(token)

        // Then
        assertThat(claims.subject).isEqualTo(userId)
    }

    @Test
    fun `parseClaims - throws for a malformed token`() {
        // When / Then
        assertThatThrownBy { JwtTokenUtils.parseClaims("not-a-valid-jwt") }
            .isInstanceOf(JwtException::class.java)
    }
}
