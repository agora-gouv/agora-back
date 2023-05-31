package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInserting
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.QagCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.annotation.EnableCaching
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@EnableCaching
@ImportAutoConfiguration(
    CacheAutoConfiguration::class,
    RedisAutoConfiguration::class,
)
internal class QagInfoRepositoryImplTest {

    @Autowired
    private lateinit var repository: QagInfoRepositoryImpl

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
        status = QagStatus.MODERATED_ACCEPTED,
        username = "username",
    )

    private val qagInserting = QagInserting(
        thematiqueId = "thematiqueId",
        title = "title",
        description = "description",
        date = Date(20),
        status = QagStatus.MODERATED_ACCEPTED,
        username = "username",
        userId = "userId",
    )

    @Nested
    inner class GetQagInfoTestCases {
        @Test
        fun `getQag - when invalid UUID - should return null`() {
            // When
            val result = repository.getQagInfo("invalid UUID")

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQag - when cacheResult is CacheNotInitialized and result not null from database- should return mapped object from database and insert it in cache`() {
            // Given
            val uuid = UUID.randomUUID()
            given(cacheRepository.getQag(uuid)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getQag(uuid)).willReturn(qagDTO)
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result = repository.getQagInfo(uuid.toString())

            // Then
            assertThat(result).isEqualTo(qagInfo)
            then(cacheRepository).should(times(1)).getQag(uuid)
            then(databaseRepository).should(only()).getQag(uuid)
            then(cacheRepository).should(times(1)).insertQag(uuid, qagDTO)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(mapper).should(only()).toDomain(qagDTO)
        }

        @Test
        fun `getQag - when cacheResult is CacheNotInitialized and result is null from database- should return null and insert QagInfoNotFound in cache`() {
            // Given
            val uuid = UUID.randomUUID()
            given(cacheRepository.getQag(uuid)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getQag(uuid)).willReturn(null)

            // When
            val result = repository.getQagInfo(uuid.toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should(times(1)).getQag(uuid)
            then(databaseRepository).should(only()).getQag(uuid)
            then(cacheRepository).should(times(1)).insertQag(uuid, null)
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQag - when cacheResult is CachedQagNotFound - should return mapped object from cache`() {
            // Given
            val uuid = UUID.randomUUID()
            given(cacheRepository.getQag(uuid)).willReturn(CacheResult.CachedQagNotFound)

            // When
            val result = repository.getQagInfo(uuid.toString())

            // Then
            assertThat(result).isEqualTo(null)
            then(cacheRepository).should(only()).getQag(uuid)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getQag - when cacheResult is CachedQag - should return mapped object from cache`() {
            // Given
            val uuid = UUID.randomUUID()
            given(cacheRepository.getQag(uuid)).willReturn(CacheResult.CachedQag(qagDTO))
            given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

            // When
            val result = repository.getQagInfo(uuid.toString())

            // Then
            assertThat(result).isEqualTo(qagInfo)
            then(cacheRepository).should(only()).getQag(uuid)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(qagDTO)
        }
    }

    @Nested
    inner class InsertQagInfoTestCases {
        @Test
        fun `insertQagInfo - when mapper returns null - should return FAILURE`() {
            given(mapper.toDto(qagInserting)).willReturn(null)

            // When
            val result = repository.insertQagInfo(qagInserting)

            // Then
            assertThat(result).isEqualTo(QagInsertionResult.Failure)
            then(databaseRepository).shouldHaveNoInteractions()
            then(cacheRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `insertQagInfo - when mapper returns DTO - should return SUCCESS`() {
            // Given
            val savedQagDTO = qagDTO.copy(id = UUID.randomUUID())
            given(mapper.toDto(qagInserting)).willReturn(qagDTO)
            given(databaseRepository.save(qagDTO)).willReturn(savedQagDTO)

            // When
            val result = repository.insertQagInfo(qagInserting)

            // Then
            assertThat(result).isEqualTo(QagInsertionResult.Success(savedQagDTO.id))
            then(databaseRepository).should(only()).save(qagDTO)
            then(cacheRepository).should(only())
                .insertQag(qagUUID = savedQagDTO.id, qagDTO = savedQagDTO)
        }
    }
}