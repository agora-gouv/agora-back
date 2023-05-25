package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.QagCacheRepository.CacheListResult
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
internal class QagLatestRepositoryImplTest {

    @Autowired
    private lateinit var repository: QagLatestRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: QagInfoDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: QagCacheRepository

    @MockBean
    private lateinit var mapper: QagInfoMapper

    private val qagDTO = QagDTO(
        id = UUID.randomUUID(),
        title = "title",
        description = "description",
        postDate = Date(42),
        status = 256,
        username = "username",
        thematiqueId = UUID.randomUUID(),
        userId = UUID.randomUUID(),
    )

    private val qagInfo = QagInfo(
        id = "id",
        thematiqueId = "thematiqueId",
        title = "title",
        description = "description",
        date = Date(14),
        status = QagStatus.MODERATED,
        username = "username",
    )

    @Test
    fun `getQagLatestList - when thematiqueId is not null and invalid UUID - should return emptyList without doing anything`() {
        // When
        val result = repository.getQagLatestList(thematiqueId = "invalid thematiqueId")

        // Then
        assertThat(result).isEqualTo(emptyList<QagInfo>())
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Nested
    inner class NullThematiqueIdCases {

        @Test
        fun `getQagLatestList - when CacheNotInitialized & database return emptyList - should call getQagLatestListFromDatabase and insert emptyList to cache`() {
            // Given
            given(cacheRepository.getQagLatestList(thematiqueId = null))
                .willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getQagLatestList()).willReturn(emptyList())

            // When
            val result = repository.getQagLatestList(thematiqueId = null)

            // Then
            assertThat(result).isEqualTo(emptyList<QagInfo>())
            then(cacheRepository).should(times(1)).getQagLatestList(thematiqueId = null)
            then(cacheRepository).should(times(1))
                .insertQagLatestList(thematiqueId = null, qagLatestList = emptyList())
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getQagLatestList()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagLatestList - when CacheNotInitialized & database return listof DTO - should call getQagLatestListFromDatabase and insert result to cache`() {
            // Given
            given(cacheRepository.getQagLatestList(thematiqueId = null))
                .willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getQagLatestList()).willReturn(listOf(qagDTO))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result = repository.getQagLatestList(thematiqueId = null)

            // Then
            assertThat(result).isEqualTo(listOf(qagInfo))
            then(cacheRepository).should(times(1)).getQagLatestList(thematiqueId = null)
            then(cacheRepository).should(times(1))
                .insertQagLatestList(thematiqueId = null, qagLatestList = listOf(qagDTO))
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getQagLatestList()
            then(mapper).should(only()).toDomain(qagDTO)
        }

        @Test
        fun `getQagLatestList - when has cache with emptyList - should return emptylist from cache`() {
            // Given
            given(cacheRepository.getQagLatestList(thematiqueId = null))
                .willReturn(CacheListResult.CachedQagList(emptyList()))

            // When
            val result = repository.getQagLatestList(thematiqueId = null)

            // Then
            assertThat(result).isEqualTo(emptyList<QagInfo>())
            then(cacheRepository).should(only()).getQagLatestList(thematiqueId = null)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagLatestList - when has cache with listof DTO - should return listof DTO from cache`() {
            // Given
            given(cacheRepository.getQagLatestList(thematiqueId = null))
                .willReturn(CacheListResult.CachedQagList(listOf(qagDTO)))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result = repository.getQagLatestList(thematiqueId = null)

            // Then
            assertThat(result).isEqualTo(listOf(qagInfo))
            then(cacheRepository).should(only()).getQagLatestList(thematiqueId = null)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(qagDTO)
        }

    }

    @Nested
    inner class NonNullThematiqueIdCases {

        private val thematiqueId = UUID.randomUUID()

        @Test
        fun `getQagLatestList - when CacheNotInitialized & database return emptyList - should call getQagLatestListFromDatabase and insert emptyList to cache`() {
            // Given
            given(cacheRepository.getQagLatestList(thematiqueId = thematiqueId))
                .willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getQagLatestListWithThematique(thematiqueId = thematiqueId)).willReturn(emptyList())

            // When
            val result = repository.getQagLatestList(thematiqueId = thematiqueId.toString())

            // Then
            assertThat(result).isEqualTo(emptyList<QagInfo>())
            then(cacheRepository).should(times(1)).getQagLatestList(thematiqueId = thematiqueId)
            then(cacheRepository).should(times(1))
                .insertQagLatestList(thematiqueId = thematiqueId, qagLatestList = emptyList())
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getQagLatestListWithThematique(thematiqueId = thematiqueId)
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagLatestList - when CacheNotInitialized & database return listof DTO - should call getQagLatestListFromDatabase and insert result to cache`() {
            // Given
            given(cacheRepository.getQagLatestList(thematiqueId = thematiqueId))
                .willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getQagLatestListWithThematique(thematiqueId = thematiqueId))
                .willReturn(listOf(qagDTO))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result = repository.getQagLatestList(thematiqueId = thematiqueId.toString())

            // Then
            assertThat(result).isEqualTo(listOf(qagInfo))
            then(cacheRepository).should(times(1)).getQagLatestList(thematiqueId = thematiqueId)
            then(cacheRepository).should(times(1))
                .insertQagLatestList(thematiqueId = thematiqueId, qagLatestList = listOf(qagDTO))
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getQagLatestListWithThematique(thematiqueId = thematiqueId)
            then(mapper).should(only()).toDomain(qagDTO)
        }

        @Test
        fun `getQagLatestList - when has cache with emptyList - should return emptylist from cache`() {
            // Given
            given(cacheRepository.getQagLatestList(thematiqueId = thematiqueId))
                .willReturn(CacheListResult.CachedQagList(emptyList()))

            // When
            val result = repository.getQagLatestList(thematiqueId = thematiqueId.toString())

            // Then
            assertThat(result).isEqualTo(emptyList<QagInfo>())
            then(cacheRepository).should(only()).getQagLatestList(thematiqueId = thematiqueId)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagLatestList - when has cache with listof DTO - should return listof DTO from cache`() {
            // Given
            given(cacheRepository.getQagLatestList(thematiqueId = thematiqueId))
                .willReturn(CacheListResult.CachedQagList(listOf(qagDTO)))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result = repository.getQagLatestList(thematiqueId = thematiqueId.toString())

            // Then
            assertThat(result).isEqualTo(listOf(qagInfo))
            then(cacheRepository).should(only()).getQagLatestList(thematiqueId = thematiqueId)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(qagDTO)
        }
    }

    @Nested
    inner class DeleteQagLatestListTestCases {
        @Test
        fun `deleteQagLatestList - when thematique has valid UUID - should delete QagLatestList from cache`() {
            //Given
            val thematiqueId = UUID.fromString("bc9e81be-eb4d-11ed-a05b-0242ac120010")

            //When
            repository.deleteQagLatestList(thematiqueId = thematiqueId.toString())

            //Then
            then(cacheRepository).should(only()).deleteQagLatestList(thematiqueId = thematiqueId)
            then(databaseRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `deleteQagLatestList - when thematique has invalid UUID - should do nothing`() {
            //When
            repository.deleteQagLatestList(thematiqueId = "1234")

            //Then
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
        }
    }
}