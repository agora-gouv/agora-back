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
internal class QagPopularRepositoryImplTest {

    @Autowired
    private lateinit var repository: QagPopularRepositoryImpl

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
        isAccepted = 1,
    )

    private val qagInfo = QagInfo(
        id = "id",
        thematiqueId = "thematiqueId",
        title = "title",
        description = "description",
        date = Date(14),
        status = QagStatus.MODERATED_ACCEPTED,
        username = "username",
    )

    @Test
    fun `getQagPopularList - when thematiqueId is not null and invalid UUID - should return emptyList without doing anything`() {
        // When
        val result = repository.getQagPopularList(thematiqueId = "invalid thematiqueId")

        // Then
        assertThat(result).isEqualTo(emptyList<QagInfo>())
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Nested
    inner class NullThematiqueIdCases {

        @Test
        fun `getQagPopularList - when CacheNotInitialized & database return emptyList - should call getQagPopularListFromDatabase and insert emptyList to cache`() {
            // Given
            given(cacheRepository.getQagPopularList(thematiqueId = null))
                .willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getQagPopularList()).willReturn(emptyList())

            // When
            val result = repository.getQagPopularList(thematiqueId = null)

            // Then
            assertThat(result).isEqualTo(emptyList<QagInfo>())
            then(cacheRepository).should(times(1)).getQagPopularList(thematiqueId = null)
            then(cacheRepository).should(times(1))
                .insertQagPopularList(thematiqueId = null, qagPopularList = emptyList())
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getQagPopularList()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagPopularList - when CacheNotInitialized & database return listof DTO - should call getQagPopularListFromDatabase and insert result to cache`() {
            // Given
            given(cacheRepository.getQagPopularList(thematiqueId = null))
                .willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getQagPopularList()).willReturn(listOf(qagDTO))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result = repository.getQagPopularList(thematiqueId = null)

            // Then
            assertThat(result).isEqualTo(listOf(qagInfo))
            then(cacheRepository).should(times(1)).getQagPopularList(thematiqueId = null)
            then(cacheRepository).should(times(1))
                .insertQagPopularList(thematiqueId = null, qagPopularList = listOf(qagDTO))
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getQagPopularList()
            then(mapper).should(only()).toDomain(qagDTO)
        }

        @Test
        fun `getQagPopularList - when has cache with emptyList - should return emptylist from cache`() {
            // Given
            given(cacheRepository.getQagPopularList(thematiqueId = null))
                .willReturn(CacheListResult.CachedQagList(emptyList()))

            // When
            val result = repository.getQagPopularList(thematiqueId = null)

            // Then
            assertThat(result).isEqualTo(emptyList<QagInfo>())
            then(cacheRepository).should(only()).getQagPopularList(thematiqueId = null)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagPopularList - when has cache with listof DTO - should return listof DTO from cache`() {
            // Given
            given(cacheRepository.getQagPopularList(thematiqueId = null))
                .willReturn(CacheListResult.CachedQagList(listOf(qagDTO)))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result = repository.getQagPopularList(thematiqueId = null)

            // Then
            assertThat(result).isEqualTo(listOf(qagInfo))
            then(cacheRepository).should(only()).getQagPopularList(thematiqueId = null)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(qagDTO)
        }

    }

    @Nested
    inner class NonNullThematiqueIdCases {

        private val thematiqueId = UUID.randomUUID()

        @Test
        fun `getQagPopularList - when CacheNotInitialized & database return emptyList - should call getQagPopularListFromDatabase and insert emptyList to cache`() {
            // Given
            given(cacheRepository.getQagPopularList(thematiqueId = thematiqueId))
                .willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getQagPopularListWithThematique(thematiqueId = thematiqueId)).willReturn(emptyList())

            // When
            val result = repository.getQagPopularList(thematiqueId = thematiqueId.toString())

            // Then
            assertThat(result).isEqualTo(emptyList<QagInfo>())
            then(cacheRepository).should(times(1)).getQagPopularList(thematiqueId = thematiqueId)
            then(cacheRepository).should(times(1))
                .insertQagPopularList(thematiqueId = thematiqueId, qagPopularList = emptyList())
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getQagPopularListWithThematique(thematiqueId = thematiqueId)
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagPopularList - when CacheNotInitialized & database return listof DTO - should call getQagPopularListFromDatabase and insert result to cache`() {
            // Given
            given(cacheRepository.getQagPopularList(thematiqueId = thematiqueId))
                .willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getQagPopularListWithThematique(thematiqueId = thematiqueId))
                .willReturn(listOf(qagDTO))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result = repository.getQagPopularList(thematiqueId = thematiqueId.toString())

            // Then
            assertThat(result).isEqualTo(listOf(qagInfo))
            then(cacheRepository).should(times(1)).getQagPopularList(thematiqueId = thematiqueId)
            then(cacheRepository).should(times(1))
                .insertQagPopularList(thematiqueId = thematiqueId, qagPopularList = listOf(qagDTO))
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getQagPopularListWithThematique(thematiqueId = thematiqueId)
            then(mapper).should(only()).toDomain(qagDTO)
        }

        @Test
        fun `getQagPopularList - when has cache with emptyList - should return emptylist from cache`() {
            // Given
            given(cacheRepository.getQagPopularList(thematiqueId = thematiqueId))
                .willReturn(CacheListResult.CachedQagList(emptyList()))

            // When
            val result = repository.getQagPopularList(thematiqueId = thematiqueId.toString())

            // Then
            assertThat(result).isEqualTo(emptyList<QagInfo>())
            then(cacheRepository).should(only()).getQagPopularList(thematiqueId = thematiqueId)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagPopularList - when has cache with listof DTO - should return listof DTO from cache`() {
            // Given
            given(cacheRepository.getQagPopularList(thematiqueId = thematiqueId))
                .willReturn(CacheListResult.CachedQagList(listOf(qagDTO)))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result = repository.getQagPopularList(thematiqueId = thematiqueId.toString())

            // Then
            assertThat(result).isEqualTo(listOf(qagInfo))
            then(cacheRepository).should(only()).getQagPopularList(thematiqueId = thematiqueId)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(qagDTO)
        }

    }

}