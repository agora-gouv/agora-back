package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness
import java.time.Clock
import java.time.Instant

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
internal class ConsultationStrapiCacheByIdRepositoryImplTest {

    @Mock
    private lateinit var clock: Clock

    private lateinit var repository: ConsultationStrapiCacheByIdRepositoryImpl

    private val consultationId = "raknhpgc9zzuplz3lu9y0huf"
    private val now = Instant.parse("2026-10-06T10:00:00Z")

    @BeforeEach
    fun setUp() {
        given(clock.instant()).willReturn(now)
        repository = ConsultationStrapiCacheByIdRepositoryImpl(clock = clock)
    }

    @Nested
    inner class `getConsultationById` {

        @Test
        fun `getConsultationById - when cache miss - should return null`() {
            // When
            val result = repository.getConsultationById(consultationId)

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `getConsultationById - when cache hit with null sentinel within TTL - should return null`() {
            // Given
            repository.putConsultationById(consultationId, null)

            // When
            val result = repository.getConsultationById(consultationId)

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `getConsultationById - when cache hit with dto within TTL - should return dto`() {
            // Given
            val dto = mock(ConsultationStrapiDTO::class.java)
            repository.putConsultationById(consultationId, dto)

            // When
            val result = repository.getConsultationById(consultationId)

            // Then
            assertThat(result).isEqualTo(dto)
        }

        @Test
        fun `getConsultationById - when cache entry is expired - should return null`() {
            // Given
            repository.putConsultationById(consultationId, mock(ConsultationStrapiDTO::class.java))
            given(clock.instant()).willReturn(now.plusSeconds(5 * 60 + 1))

            // When
            val result = repository.getConsultationById(consultationId)

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `getConsultationById - when cache entry is exactly at TTL boundary - should return null`() {
            // Given
            repository.putConsultationById(consultationId, mock(ConsultationStrapiDTO::class.java))
            given(clock.instant()).willReturn(now.plusSeconds(5 * 60))

            // When
            val result = repository.getConsultationById(consultationId)

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `getConsultationById - when cache entry is just before TTL boundary - should return dto`() {
            // Given
            val dto = mock(ConsultationStrapiDTO::class.java)
            repository.putConsultationById(consultationId, dto)
            given(clock.instant()).willReturn(now.plusSeconds(5 * 60 - 1))

            // When
            val result = repository.getConsultationById(consultationId)

            // Then
            assertThat(result).isEqualTo(dto)
        }
    }

    @Nested
    inner class `putConsultationById` {

        @Test
        fun `putConsultationById - when dto is not null - should store and return it on next get`() {
            // Given
            val dto = mock(ConsultationStrapiDTO::class.java)

            // When
            repository.putConsultationById(consultationId, dto)
            val result = repository.getConsultationById(consultationId)

            // Then
            assertThat(result).isEqualTo(dto)
        }

        @Test
        fun `putConsultationById - when dto is null - should store null sentinel and return null on next get`() {
            // When
            repository.putConsultationById(consultationId, null)
            val result = repository.getConsultationById(consultationId)

            // Then
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class `getConsultationByIdWithUnpublished` {

        @Test
        fun `getConsultationByIdWithUnpublished - when cache miss - should return null`() {
            // When
            val result = repository.getConsultationByIdWithUnpublished(consultationId)

            // Then
            assertThat(result).isNull()
        }

        @Test
        fun `getConsultationByIdWithUnpublished - when cache hit with dto within TTL - should return dto`() {
            // Given
            val dto = mock(ConsultationStrapiDTO::class.java)
            repository.putConsultationByIdWithUnpublished(consultationId, dto)

            // When
            val result = repository.getConsultationByIdWithUnpublished(consultationId)

            // Then
            assertThat(result).isEqualTo(dto)
        }

        @Test
        fun `getConsultationByIdWithUnpublished - when cache entry is expired - should return null`() {
            // Given
            repository.putConsultationByIdWithUnpublished(consultationId, mock(ConsultationStrapiDTO::class.java))
            given(clock.instant()).willReturn(now.plusSeconds(5 * 60 + 1))

            // When
            val result = repository.getConsultationByIdWithUnpublished(consultationId)

            // Then
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class `putConsultationByIdWithUnpublished` {

        @Test
        fun `putConsultationByIdWithUnpublished - when dto is not null - should store and return it on next get`() {
            // Given
            val dto = mock(ConsultationStrapiDTO::class.java)

            // When
            repository.putConsultationByIdWithUnpublished(consultationId, dto)
            val result = repository.getConsultationByIdWithUnpublished(consultationId)

            // Then
            assertThat(result).isEqualTo(dto)
        }

        @Test
        fun `putConsultationByIdWithUnpublished - when dto is null - should store null sentinel and return null on next get`() {
            // When
            repository.putConsultationByIdWithUnpublished(consultationId, null)
            val result = repository.getConsultationByIdWithUnpublished(consultationId)

            // Then
            assertThat(result).isNull()
        }
    }

    @Nested
    inner class `evictConsultationById` {

        @Test
        fun `evictConsultationById - should remove entry from byId cache`() {
            // Given
            val dto = mock(ConsultationStrapiDTO::class.java)
            repository.putConsultationById(consultationId, dto)

            // When
            repository.evictConsultationById(consultationId)

            // Then
            assertThat(repository.getConsultationById(consultationId)).isNull()
        }

        @Test
        fun `evictConsultationById - should remove entry from withUnpublished cache`() {
            // Given
            val dto = mock(ConsultationStrapiDTO::class.java)
            repository.putConsultationByIdWithUnpublished(consultationId, dto)

            // When
            repository.evictConsultationById(consultationId)

            // Then
            assertThat(repository.getConsultationByIdWithUnpublished(consultationId)).isNull()
        }

        @Test
        fun `evictConsultationById - should evict from both caches independently`() {
            // Given
            val dto = mock(ConsultationStrapiDTO::class.java)
            repository.putConsultationById(consultationId, dto)
            repository.putConsultationByIdWithUnpublished(consultationId, dto)

            // When
            repository.evictConsultationById(consultationId)

            // Then
            assertThat(repository.getConsultationById(consultationId)).isNull()
            assertThat(repository.getConsultationByIdWithUnpublished(consultationId)).isNull()
        }

        @Test
        fun `evictConsultationById - when evicting one id - should not affect other ids in cache`() {
            // Given
            val dto = mock(ConsultationStrapiDTO::class.java)
            val otherId = "other-consultation-id"
            repository.putConsultationById(consultationId, dto)
            repository.putConsultationById(otherId, dto)

            // When
            repository.evictConsultationById(consultationId)

            // Then
            assertThat(repository.getConsultationById(consultationId)).isNull()
            assertThat(repository.getConsultationById(otherId)).isEqualTo(dto)
        }
    }
}
