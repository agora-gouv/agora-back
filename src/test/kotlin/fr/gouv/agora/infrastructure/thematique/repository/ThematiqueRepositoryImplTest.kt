package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueStrapiDTO
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueCacheRepository.CacheListResult
import fr.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.mock
import org.mockito.BDDMockito.only
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.UUID

@ExtendWith(MockitoExtension::class)
internal class ThematiqueRepositoryImplTest {

    @InjectMocks
    private lateinit var repository: ThematiqueRepositoryImpl

    @Mock
    private lateinit var cacheRepository: ThematiqueCacheRepository

    @Mock
    private lateinit var databaseRepository: ThematiqueDatabaseRepository

    @Mock
    private lateinit var strapiRepository: ThematiqueStrapiRepository

    @Mock
    private lateinit var featureFlagsRepository: FeatureFlagsRepository

    @Mock
    private lateinit var mapper: ThematiqueMapper

    @Nested
    inner class GetThematiqueListCases {

        @Test
        fun `getThematiqueList - when cache returns CachedThematiqueList - should return mapped dtos from cache`() {
            // Given
            val thematique = mock(Thematique::class.java)
            given(cacheRepository.getThematiqueList())
                .willReturn(CacheListResult.CachedThematiqueList(listOf(thematique)))

            // When
            val result = repository.getThematiqueList()

            // Then
            assertThat(result).isEqualTo(listOf(thematique))
            then(cacheRepository).should(only()).getThematiqueList()
            then(databaseRepository).shouldHaveNoInteractions()
            then(strapiRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `getThematiqueList - when cache returns CacheNotInitialized and database returns emptyList - should not insert to cache then return emptyList`() {
            // Given
            given(cacheRepository.getThematiqueList()).willReturn(CacheListResult.CacheNotInitialized)

            val thematiqueStrapiDTO = mock(StrapiDTO::class.java) as StrapiDTO<ThematiqueStrapiDTO>
            given(strapiRepository.getThematiques()).willReturn(thematiqueStrapiDTO)
            given(mapper.toDomain(thematiqueStrapiDTO)).willReturn(emptyList())
            given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiThematiques)).willReturn(true)

            // When
            val result = repository.getThematiqueList()

            // Then
            assertThat(result).isEqualTo(emptyList<Thematique>())
            then(cacheRepository).should().getThematiqueList()
            then(cacheRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `getThematiqueList - when cache returns CacheNotInitialized and database returns something - should insert to cache then return parsed dto from database`() {
            // Given
            val strapiThematiqueDTO = mock(StrapiDTO::class.java) as StrapiDTO<ThematiqueStrapiDTO>
            val thematique = mock(Thematique::class.java)
            given(cacheRepository.getThematiqueList()).willReturn(CacheListResult.CacheNotInitialized)
            given(strapiRepository.getThematiques()).willReturn(strapiThematiqueDTO)
            given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiThematiques)).willReturn(true)

            given(mapper.toDomain(strapiThematiqueDTO)).willReturn(listOf(thematique))

            // When
            val result = repository.getThematiqueList()

            // Then
            assertThat(result).isEqualTo(listOf(thematique))
            then(cacheRepository).should().getThematiqueList()
            then(cacheRepository).should().insertThematiqueList(listOf(thematique))
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(strapiRepository).should(only()).getThematiques()
        }

    }

    @Nested
    inner class GetThematiqueCases {

        @Test
        fun `getThematique - when cache returns CachedThematiqueList and has matching thematique - should return mapped dto from cache`() {
            // Given
            val thematiqueId = UUID.randomUUID()

            val thematique = mock(Thematique::class.java).also {
                given(it.id).willReturn(thematiqueId.toString())
            }
            given(cacheRepository.getThematiqueList())
                .willReturn(CacheListResult.CachedThematiqueList(listOf(thematique)))

            // When
            val result = repository.getThematique(thematiqueId.toString())

            // Then
            assertThat(result).isEqualTo(thematique)
            then(cacheRepository).should(only()).getThematiqueList()
        }

        @Test
        fun `getThematique - when cache returns CachedThematiqueList and has no matching thematique - should return null`() {
            // Given
            val thematique = mock(Thematique::class.java).also {
                given(it.id).willReturn(NOT_FOUND_UUID.toString())
            }
            given(cacheRepository.getThematiqueList())
                .willReturn(CacheListResult.CachedThematiqueList(listOf(thematique)))

            // When
            val result = repository.getThematique(UUID.randomUUID().toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should(only()).getThematiqueList()
            then(strapiRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getThematique - when cache returns CacheNotInitialized and database return a matching thematique - should insert dto to cache then return mapped dto from database`() {
            // Given
            val thematiqueId = UUID.randomUUID().toString()

            given(cacheRepository.getThematiqueList()).willReturn(CacheListResult.CacheNotInitialized)

            val thematiqueStrapiDTO = mock(StrapiDTO::class.java) as StrapiDTO<ThematiqueStrapiDTO>
            given(strapiRepository.getThematiques()).willReturn(thematiqueStrapiDTO)

            val thematique = mock(Thematique::class.java).also {
                given(it.id).willReturn(thematiqueId)
            }
            given(mapper.toDomain(thematiqueStrapiDTO)).willReturn(listOf(thematique))

            given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiThematiques)).willReturn(true)

            // When
            val result = repository.getThematique(thematiqueId)

            // Then
            assertThat(result).isEqualTo(thematique)
            then(cacheRepository).should().insertThematiqueList(listOf(thematique))
        }

        @Test
        fun `getThematique - when cache returns CacheNotInitialized and database return no matching thematique - should insert dto to cache then return mapped dto from database`() {
            // Given
            val thematiqueStrapiDTO = mock(StrapiDTO::class.java) as StrapiDTO<ThematiqueStrapiDTO>
            val thematique = mock(Thematique::class.java)

            given(strapiRepository.getThematiques()).willReturn(thematiqueStrapiDTO)
            given(mapper.toDomain(thematiqueStrapiDTO)).willReturn(listOf(thematique))

            given(cacheRepository.getThematiqueList()).willReturn(CacheListResult.CacheNotInitialized)
            given(featureFlagsRepository.isFeatureEnabled(AgoraFeature.StrapiThematiques)).willReturn(true)

            // When
            val result = repository.getThematique(UUID.randomUUID().toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should().getThematiqueList()
            then(cacheRepository).should().insertThematiqueList(listOf(thematique))
            then(cacheRepository).shouldHaveNoMoreInteractions()
        }
    }
}
