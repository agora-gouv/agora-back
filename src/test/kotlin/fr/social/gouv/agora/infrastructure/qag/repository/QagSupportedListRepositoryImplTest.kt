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
internal class QagSupportedListRepositoryImplTest {

    @Autowired
    private lateinit var repository: QagSupportedListRepositoryImpl

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

    private val userUUID = UUID.fromString("bc9e81be-eb4d-11ed-a05b-0242ac120003")

    @Test
    fun `getQagSupportedList - when thematiqueId is not null and invalid UUID and userId is valid UUID- should return emptyList without doing anything`() {
        // When
        val result = repository.getQagSupportedList(thematiqueId = "invalid thematiqueId", userId = userUUID.toString())

        // Then
        assertThat(result).isEqualTo(emptyList<QagInfo>())
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagSupportedList - when thematiqueId is valid UUID and userId is not null and invalid UUID- should return emptyList without doing anything`() {
        // When
        val result =
            repository.getQagSupportedList(thematiqueId = UUID.randomUUID().toString(), userId = "invalid userId")

        // Then
        assertThat(result).isEqualTo(emptyList<QagInfo>())
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Nested
    inner class NullThematiqueIdCases {

        @Test
        fun `getQagSupportedList - when CacheNotInitialized & database return emptyList - should call getQagSupportedListFromDatabase and insert emptyList to cache`() {
            // Given
            given(cacheRepository.getQagSupportedList(thematiqueId = null, userId = userUUID))
                .willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getQagSupportedList(userId = userUUID)).willReturn(emptyList())

            // When
            val result = repository.getQagSupportedList(thematiqueId = null, userId = userUUID.toString())

            // Then
            assertThat(result).isEqualTo(emptyList<QagInfo>())
            then(cacheRepository).should(times(1)).getQagSupportedList(thematiqueId = null, userId = userUUID)
            then(cacheRepository).should(times(1))
                .insertQagSupportedList(thematiqueId = null, qagSupportedList = emptyList(), userId = userUUID)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getQagSupportedList(userId = userUUID)
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagSupportedList - when CacheNotInitialized & database return listof DTO - should call getQagSupportedListFromDatabase and insert result to cache`() {
            // Given
            given(cacheRepository.getQagSupportedList(thematiqueId = null, userId = userUUID))
                .willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getQagSupportedList(userUUID)).willReturn(listOf(qagDTO))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result = repository.getQagSupportedList(thematiqueId = null, userId = userUUID.toString())

            // Then
            assertThat(result).isEqualTo(listOf(qagInfo))
            then(cacheRepository).should(times(1)).getQagSupportedList(thematiqueId = null, userId = userUUID)
            then(cacheRepository).should(times(1))
                .insertQagSupportedList(thematiqueId = null, qagSupportedList = listOf(qagDTO), userId = userUUID)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getQagSupportedList(userUUID)
            then(mapper).should(only()).toDomain(qagDTO)
        }

        @Test
        fun `getQagSupportedList - when has cache with emptyList - should return emptylist from cache`() {
            // Given
            given(cacheRepository.getQagSupportedList(thematiqueId = null, userId = userUUID))
                .willReturn(CacheListResult.CachedQagList(emptyList()))

            // When
            val result = repository.getQagSupportedList(thematiqueId = null, userId = userUUID.toString())

            // Then
            assertThat(result).isEqualTo(emptyList<QagInfo>())
            then(cacheRepository).should(only()).getQagSupportedList(thematiqueId = null, userId = userUUID)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagSupportedList - when has cache with listof DTO - should return listof DTO from cache`() {
            // Given
            given(cacheRepository.getQagSupportedList(thematiqueId = null, userId = userUUID))
                .willReturn(CacheListResult.CachedQagList(listOf(qagDTO)))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result = repository.getQagSupportedList(thematiqueId = null, userId = userUUID.toString())

            // Then
            assertThat(result).isEqualTo(listOf(qagInfo))
            then(cacheRepository).should(only()).getQagSupportedList(thematiqueId = null, userId = userUUID)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(qagDTO)
        }

    }

    @Nested
    inner class NonNullThematiqueIdCases {

        private val thematiqueId = UUID.randomUUID()

        @Test
        fun `getQagSupportedList - when CacheNotInitialized & database return emptyList - should call getQagSupportedListFromDatabase and insert emptyList to cache`() {
            // Given
            given(cacheRepository.getQagSupportedList(thematiqueId = thematiqueId, userId = userUUID))
                .willReturn(CacheListResult.CacheNotInitialized)
            given(
                databaseRepository.getQagSupportedListWithThematique(
                    thematiqueId = thematiqueId,
                    userId = userUUID
                )
            ).willReturn(emptyList())

            // When
            val result =
                repository.getQagSupportedList(thematiqueId = thematiqueId.toString(), userId = userUUID.toString())

            // Then
            assertThat(result).isEqualTo(emptyList<QagInfo>())
            then(cacheRepository).should(times(1)).getQagSupportedList(thematiqueId = thematiqueId, userId = userUUID)
            then(cacheRepository).should(times(1))
                .insertQagSupportedList(thematiqueId = thematiqueId, qagSupportedList = emptyList(), userId = userUUID)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only())
                .getQagSupportedListWithThematique(thematiqueId = thematiqueId, userId = userUUID)
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagSupportedList - when CacheNotInitialized & database return listof DTO - should call getQagSupportedListFromDatabase and insert result to cache`() {
            // Given
            given(cacheRepository.getQagSupportedList(thematiqueId = thematiqueId, userId = userUUID))
                .willReturn(CacheListResult.CacheNotInitialized)
            given(databaseRepository.getQagSupportedListWithThematique(thematiqueId = thematiqueId, userId = userUUID))
                .willReturn(listOf(qagDTO))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result =
                repository.getQagSupportedList(thematiqueId = thematiqueId.toString(), userId = userUUID.toString())

            // Then
            assertThat(result).isEqualTo(listOf(qagInfo))
            then(cacheRepository).should(times(1)).getQagSupportedList(thematiqueId = thematiqueId, userId = userUUID)
            then(cacheRepository).should(times(1))
                .insertQagSupportedList(
                    thematiqueId = thematiqueId,
                    qagSupportedList = listOf(qagDTO),
                    userId = userUUID
                )
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only())
                .getQagSupportedListWithThematique(thematiqueId = thematiqueId, userId = userUUID)
            then(mapper).should(only()).toDomain(qagDTO)
        }

        @Test
        fun `getQagSupportedList - when has cache with emptyList - should return emptylist from cache`() {
            // Given
            given(cacheRepository.getQagSupportedList(thematiqueId = thematiqueId, userId = userUUID))
                .willReturn(CacheListResult.CachedQagList(emptyList()))

            // When
            val result =
                repository.getQagSupportedList(thematiqueId = thematiqueId.toString(), userId = userUUID.toString())

            // Then
            assertThat(result).isEqualTo(emptyList<QagInfo>())
            then(cacheRepository).should(only()).getQagSupportedList(thematiqueId = thematiqueId, userId = userUUID)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQagSupportedList - when has cache with listof DTO - should return listof DTO from cache`() {
            // Given
            given(cacheRepository.getQagSupportedList(thematiqueId = thematiqueId, userId = userUUID))
                .willReturn(CacheListResult.CachedQagList(listOf(qagDTO)))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result =
                repository.getQagSupportedList(thematiqueId = thematiqueId.toString(), userId = userUUID.toString())

            // Then
            assertThat(result).isEqualTo(listOf(qagInfo))
            then(cacheRepository).should(only()).getQagSupportedList(thematiqueId = thematiqueId, userId = userUUID)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(qagDTO)
        }
    }

    @Test
    fun `deleteQagSupportedList - when thematique has valid UUID - should delete QagSupportedList from cache`() {
        //Given
        val thematiqueId = UUID.fromString("bc9e81be-eb4d-11ed-a05b-0242ac120010")
        //When
        repository.deleteQagSupportedList(thematiqueId= thematiqueId.toString(), userId = userUUID.toString())

        //Then
        then(cacheRepository).should(only()).deleteQagSupportedList(thematiqueId = thematiqueId, userId = userUUID)
        then(databaseRepository).shouldHaveNoInteractions()
    }
    @Test
    fun `deleteQagSupportedList - when thematique has invalid UUID - should do nothing`() {
        //Given
        val thematiqueId = "1234"
        //When
        repository.deleteQagSupportedList(thematiqueId= thematiqueId, userId = userUUID.toString())

        //Then
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
    }
    @Test
    fun `deleteQagSupportedList - when userId is invalid UUID and thematiqueId is valid UUID - should do nothing`() {
        //Given
        val thematiqueId = "bc9e81be-eb4d-11ed-a05b-0242ac120002"
        //When
        repository.deleteQagSupportedList(thematiqueId= thematiqueId, userId = "1234")

        //Then
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
    }
}