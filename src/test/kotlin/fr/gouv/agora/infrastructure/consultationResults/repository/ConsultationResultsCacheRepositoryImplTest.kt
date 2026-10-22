package fr.gouv.agora.infrastructure.consultationResults.repository

import fr.gouv.agora.domain.ConsultationResults
import fr.gouv.agora.usecase.consultationResults.repository.ConsultationResultsCacheResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Clock
import java.time.Instant

@ExtendWith(MockitoExtension::class)
internal class ConsultationResultsCacheRepositoryImplTest {

    private lateinit var repository: ConsultationResultsCacheRepositoryImpl
    private lateinit var clock: Clock

    @BeforeEach
    fun setUp() {
        clock = mock(Clock::class.java)
        repository = ConsultationResultsCacheRepositoryImpl(clock)
    }

    @Nested
    inner class GetConsultationResults {

        @Test
        fun `getConsultationResults - when cache is empty - should return CacheNotInitialized`() {
            // When
            val result = repository.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized)
        }

        @Test
        fun `getConsultationResults - when cache has results and not expired - should return CachedConsultationResults`() {
            // Given
            val now = Instant.parse("2026-01-01T10:00:00Z")
            given(clock.instant()).willReturn(now)

            val results = mock(ConsultationResults::class.java)
            repository.initConsultationResults(consultationId = "consultationId", results = results)

            // advance time by 30 minutes (within TTL)
            val thirtyMinutesLater = now.plusSeconds(30 * 60)
            given(clock.instant()).willReturn(thirtyMinutesLater)

            // When
            val result = repository.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(ConsultationResultsCacheResult.CachedConsultationResults(results))
        }

        @Test
        fun `getConsultationResults - when cache has not found sentinel and not expired - should return ConsultationResultsNotFound`() {
            // Given
            val now = Instant.parse("2026-01-01T10:00:00Z")
            given(clock.instant()).willReturn(now)

            repository.initConsultationResultsNotFound(consultationId = "consultationId")

            // advance time by 30 minutes (within TTL)
            val thirtyMinutesLater = now.plusSeconds(30 * 60)
            given(clock.instant()).willReturn(thirtyMinutesLater)

            // When
            val result = repository.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(ConsultationResultsCacheResult.ConsultationResultsNotFound)
        }

        @Test
        fun `getConsultationResults - when cache entry is expired - should return CacheNotInitialized`() {
            // Given
            val now = Instant.parse("2026-01-01T10:00:00Z")
            given(clock.instant()).willReturn(now)

            val results = mock(ConsultationResults::class.java)
            repository.initConsultationResults(consultationId = "consultationId", results = results)

            // advance time by 2 hours (past TTL of 1h)
            val twoHoursLater = now.plusSeconds(2 * 60 * 60)
            given(clock.instant()).willReturn(twoHoursLater)

            // When
            val result = repository.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized)
        }

        @Test
        fun `getConsultationResults - when cache entry is expired - second call should also return CacheNotInitialized`() {
            // Given
            val now = Instant.parse("2026-01-01T10:00:00Z")
            given(clock.instant()).willReturn(now)

            val results = mock(ConsultationResults::class.java)
            repository.initConsultationResults(consultationId = "consultationId", results = results)

            val twoHoursLater = now.plusSeconds(2 * 60 * 60)
            given(clock.instant()).willReturn(twoHoursLater)
            // first call triggers eviction
            repository.getConsultationResults(consultationId = "consultationId")

            // When - second call after eviction
            val result = repository.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized)
        }

        @Test
        fun `getConsultationResults - when different consultationIds - should return independent results`() {
            // Given
            val now = Instant.parse("2026-01-01T10:00:00Z")
            given(clock.instant()).willReturn(now)

            val results1 = mock(ConsultationResults::class.java)
            val results2 = mock(ConsultationResults::class.java)
            repository.initConsultationResults(consultationId = "consultationId1", results = results1)
            repository.initConsultationResults(consultationId = "consultationId2", results = results2)

            // When
            val result1 = repository.getConsultationResults(consultationId = "consultationId1")
            val result2 = repository.getConsultationResults(consultationId = "consultationId2")

            // Then
            assertThat(result1).isEqualTo(ConsultationResultsCacheResult.CachedConsultationResults(results1))
            assertThat(result2).isEqualTo(ConsultationResultsCacheResult.CachedConsultationResults(results2))
        }
    }

    @Nested
    inner class EvictConsultationResultsCache {

        @Test
        fun `evictConsultationResultsCache - when entry exists - should return CacheNotInitialized after eviction`() {
            // Given
            val now = Instant.parse("2026-01-01T10:00:00Z")
            given(clock.instant()).willReturn(now)

            val results = mock(ConsultationResults::class.java)
            repository.initConsultationResults(consultationId = "consultationId", results = results)

            // When
            repository.evictConsultationResultsCache(consultationId = "consultationId")
            val result = repository.getConsultationResults(consultationId = "consultationId")

            // Then
            assertThat(result).isEqualTo(ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized)
        }

        @Test
        fun `evictConsultationResultsCache - when evicting one id - should not affect other ids`() {
            // Given
            val now = Instant.parse("2026-01-01T10:00:00Z")
            given(clock.instant()).willReturn(now)

            val results1 = mock(ConsultationResults::class.java)
            val results2 = mock(ConsultationResults::class.java)
            repository.initConsultationResults(consultationId = "consultationId1", results = results1)
            repository.initConsultationResults(consultationId = "consultationId2", results = results2)

            // When
            repository.evictConsultationResultsCache(consultationId = "consultationId1")

            // Then
            assertThat(repository.getConsultationResults(consultationId = "consultationId1"))
                .isEqualTo(ConsultationResultsCacheResult.ConsultationResultsCacheNotInitialized)
            assertThat(repository.getConsultationResults(consultationId = "consultationId2"))
                .isEqualTo(ConsultationResultsCacheResult.CachedConsultationResults(results2))
        }
    }
}
