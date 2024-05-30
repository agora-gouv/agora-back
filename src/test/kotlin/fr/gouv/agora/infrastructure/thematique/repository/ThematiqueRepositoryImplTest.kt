package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.responseQag.repository.ThematiqueStrapiRepository
import fr.gouv.agora.infrastructure.thematique.dto.StrapiThematiqueDTO
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueCacheRepository.CacheListResult
import fr.gouv.agora.infrastructure.utils.UuidUtils.NOT_FOUND_UUID
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

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
        fun `getThematiqueList - when cache returns CacheNotInitialized and database returns emptyList - should insert to cache then return emptyList`() {
            // Given
            given(cacheRepository.getThematiqueList()).willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getThematiqueList()).willReturn(emptyList())
            given(strapiRepository.getThematiques()).willReturn(StrapiDTO.ofEmpty())

            // When
            val result = repository.getThematiqueList()

            // Then
            assertThat(result).isEqualTo(emptyList<Thematique>())
            then(cacheRepository).should().getThematiqueList()
            then(cacheRepository).should().insertThematiqueList(emptyList())
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getThematiqueList()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getThematiqueList - when cache returns CacheNotInitialized and database returns something - should insert to cache then return parsed dto from database`() {
            // Given
            val databaseThematiqueDTO = mock(ThematiqueDTO::class.java)
            val strapiThematiqueDTO = mock(StrapiDTO::class.java) as StrapiDTO<StrapiThematiqueDTO>
            val thematique = mock(Thematique::class.java)
            given(cacheRepository.getThematiqueList()).willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getThematiqueList()).willReturn(listOf(databaseThematiqueDTO))
            given(strapiRepository.getThematiques()).willReturn(strapiThematiqueDTO)

            given(mapper.toDomain(databaseThematiqueDTO)).willReturn(thematique)

            // When
            val result = repository.getThematiqueList()

            // Then
            assertThat(result).isEqualTo(listOf(thematique))
            then(cacheRepository).should().getThematiqueList()
            then(cacheRepository).should().insertThematiqueList(listOf(thematique))
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getThematiqueList()
            then(mapper).should(only()).toDomain(databaseThematiqueDTO)
        }

    }

    @Nested
    inner class GetThematiqueCases {

        @Test
        fun `getThematique - when invalid thematique UUID - should return null without checking neither cache nor database`() {
            // When
            val result = repository.getThematique("invalid thematiqueId")

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getThematique - when cache returns CachedThematiqueList and has matching thematique - should return mapped dto from cache`() {
            // Given
            val thematiqueId = UUID.randomUUID()
            val thematiqueDTO = mock(ThematiqueDTO::class.java).also {
                given(it.id).willReturn(thematiqueId)
            }

            val thematique = mock(Thematique::class.java)
            given(cacheRepository.getThematiqueList())
                .willReturn(CacheListResult.CachedThematiqueList(listOf(thematique)))
            given(mapper.toDomain(thematiqueDTO)).willReturn(thematique)

            // When
            val result = repository.getThematique(thematiqueId.toString())

            // Then
            assertThat(result).isEqualTo(thematique)
            then(cacheRepository).should(only()).getThematiqueList()
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(thematiqueDTO)
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
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getThematique - when cache returns CacheNotInitialized and database return a matching thematique - should insert dto to cache then return mapped dto from database`() {
            // Given
            val thematiqueId = UUID.randomUUID()
            val thematiqueDTO = mock(ThematiqueDTO::class.java).also {
                given(it.id).willReturn(thematiqueId)
            }
            given(cacheRepository.getThematiqueList()).willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getThematiqueList()).willReturn(listOf(thematiqueDTO))
            val thematiqueStrapiDTO = mock(StrapiDTO::class.java) as StrapiDTO<StrapiThematiqueDTO>
            given(strapiRepository.getThematiques()).willReturn(thematiqueStrapiDTO)

            val thematique = mock(Thematique::class.java)
            given(mapper.toDomain(thematiqueDTO)).willReturn(thematique)

            // When
            val result = repository.getThematique(thematiqueId.toString())

            // Then
            assertThat(result).isEqualTo(thematique)
            then(cacheRepository).should().insertThematiqueList(listOf(thematique))
            then(databaseRepository).should(only()).getThematiqueList()
            then(mapper).should(only()).toDomain(thematiqueDTO)
        }

        @Test
        fun `getThematique - when cache returns CacheNotInitialized and database return no matching thematique - should insert dto to cache then return mapped dto from database`() {
            // Given
            val thematiqueDTO = mock(ThematiqueDTO::class.java).also {
                given(it.id).willReturn(NOT_FOUND_UUID)
            }
            given(cacheRepository.getThematiqueList()).willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getThematiqueList()).willReturn(listOf(thematiqueDTO))
            val thematique = mock(Thematique::class.java)

            // When
            val result = repository.getThematique(UUID.randomUUID().toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should().getThematiqueList()
            then(cacheRepository).should().insertThematiqueList(listOf(thematique))
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getThematiqueList()
            then(mapper).shouldHaveNoInteractions()
        }
    }
}
