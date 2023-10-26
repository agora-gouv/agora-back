package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import fr.gouv.agora.infrastructure.thematique.repository.ThematiqueCacheRepository.CacheListResult
import fr.gouv.agora.infrastructure.utils.UuidUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ThematiqueRepositoryImplTest {

    @Autowired
    private lateinit var repository: ThematiqueRepositoryImpl

    @MockBean
    private lateinit var cacheRepository: ThematiqueCacheRepository

    @MockBean
    private lateinit var databaseRepository: ThematiqueDatabaseRepository

    @MockBean
    private lateinit var mapper: ThematiqueMapper

    @Nested
    inner class GetThematiqueListCases {

        @Test
        fun `getThematiqueList - when cache returns CachedThematiqueList - should return mapped dtos from cache`() {
            // Given
            val thematiqueDTO = mock(ThematiqueDTO::class.java)
            given(cacheRepository.getThematiqueList())
                .willReturn(CacheListResult.CachedThematiqueList(listOf(thematiqueDTO)))

            val thematique = mock(Thematique::class.java)
            given(mapper.toDomain(thematiqueDTO)).willReturn(thematique)

            // When
            val result = repository.getThematiqueList()

            // Then
            assertThat(result).isEqualTo(listOf(thematique))
            then(cacheRepository).should(only()).getThematiqueList()
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(thematiqueDTO)
        }

        @Test
        fun `getThematiqueList - when cache returns CacheNotInitialized and database returns emptyList - should insert to cache then return emptyList`() {
            // Given
            given(cacheRepository.getThematiqueList()).willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getThematiqueList()).willReturn(emptyList())

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
            val thematiqueDTO = mock(ThematiqueDTO::class.java)
            given(cacheRepository.getThematiqueList()).willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getThematiqueList()).willReturn(listOf(thematiqueDTO))

            val thematique = mock(Thematique::class.java)
            given(mapper.toDomain(thematiqueDTO)).willReturn(thematique)

            // When
            val result = repository.getThematiqueList()

            // Then
            assertThat(result).isEqualTo(listOf(thematique))
            then(cacheRepository).should().getThematiqueList()
            then(cacheRepository).should().insertThematiqueList(listOf(thematiqueDTO))
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getThematiqueList()
            then(mapper).should(only()).toDomain(thematiqueDTO)
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
            val thematiqueDTO = createThematiqueDTO(id = thematiqueId)
            given(cacheRepository.getThematiqueList())
                .willReturn(CacheListResult.CachedThematiqueList(listOf(thematiqueDTO)))

            val thematique = mock(Thematique::class.java)
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
            val thematiqueDTO = createThematiqueDTO(id = UuidUtils.NOT_FOUND_UUID)
            given(cacheRepository.getThematiqueList())
                .willReturn(CacheListResult.CachedThematiqueList(listOf(thematiqueDTO)))

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
            val thematiqueDTO = createThematiqueDTO(id = thematiqueId)
            given(cacheRepository.getThematiqueList()).willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getThematiqueList()).willReturn(listOf(thematiqueDTO))

            val thematique = mock(Thematique::class.java)
            given(mapper.toDomain(thematiqueDTO)).willReturn(thematique)

            // When
            val result = repository.getThematique(thematiqueId.toString())

            // Then
            assertThat(result).isEqualTo(thematique)
            then(cacheRepository).should().getThematiqueList()
            then(cacheRepository).should().insertThematiqueList(listOf(thematiqueDTO))
            then(databaseRepository).should(only()).getThematiqueList()
            then(mapper).should(only()).toDomain(thematiqueDTO)
        }

        @Test
        fun `getThematique - when cache returns CacheNotInitialized and database return no matching thematique - should insert dto to cache then return mapped dto from database`() {
            // Given
            val thematiqueDTO = createThematiqueDTO(id = UuidUtils.NOT_FOUND_UUID)
            given(cacheRepository.getThematiqueList()).willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getThematiqueList()).willReturn(listOf(thematiqueDTO))

            // When
            val result = repository.getThematique(UUID.randomUUID().toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should().getThematiqueList()
            then(cacheRepository).should().insertThematiqueList(listOf(thematiqueDTO))
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getThematiqueList()
            then(mapper).shouldHaveNoInteractions()
        }

        private fun createThematiqueDTO(id: UUID) = mock(ThematiqueDTO::class.java).also {
            given(it.id).willReturn(id)
        }

    }

}