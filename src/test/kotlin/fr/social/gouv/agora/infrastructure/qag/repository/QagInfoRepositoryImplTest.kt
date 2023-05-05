package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInfo
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.QagCacheRepository.CacheResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
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