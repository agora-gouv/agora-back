package fr.gouv.agora.infrastructure.supportQag.repository

import fr.gouv.agora.domain.SupportQagInserting
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class SupportQagInsertingMapperTest {

    @InjectMocks
    private lateinit var mapper: SupportQagMapper

    @Test
    fun `toDto - when invalid UUID for qagId - should return null`() {
        // Given
        val supportQagInserting = SupportQagInserting(
            qagId = "qagId with invalid UUID",
            userId = "userId",
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
            qagId = "fda60299-fe2d-4282-bb45-284dcb4fa7ee",
            userId = "bc9e81be-eb4d-11ed-a05b-0242ac120003",
        )

        // When
        val result = mapper.toDto(supportQagInserting)

        // Then
        assertThat(result?.userId).isEqualTo(UUID.fromString("bc9e81be-eb4d-11ed-a05b-0242ac120003"))
        assertThat(result?.qagId).isEqualTo(UUID.fromString("fda60299-fe2d-4282-bb45-284dcb4fa7ee"))
    }
}