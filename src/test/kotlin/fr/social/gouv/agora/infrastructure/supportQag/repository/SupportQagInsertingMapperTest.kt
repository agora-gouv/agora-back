package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQagInserting
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class SupportQagInsertingMapperTest {

    @Autowired
    private lateinit var mapper: SupportQagMapper

    @Test
    fun `toDto - when invalid UUID for qagId - should return null`() {
        // Given
        val supportQagInserting = SupportQagInserting(
            userId = "userId",
            qagId = "qagId with invalid UUID",
        )

        // When
        val result = mapper.toDto(supportQagInserting)

        // Then
        assertThat(result).isEqualTo(null)
    }

    @Test
    fun `toDto - when valid UUID - should return DTO`() {
        // Given
        val supportQagInserting = SupportQagInserting(
            userId = "userId",
            qagId = "fda60299-fe2d-4282-bb45-284dcb4fa7ee",
        )

        // When
        val result = mapper.toDto(supportQagInserting)

        // Then
        assertThat(result?.userId).isEqualTo("userId")
        assertThat(result?.qagId).isEqualTo(UUID.fromString("fda60299-fe2d-4282-bb45-284dcb4fa7ee"))
    }
}