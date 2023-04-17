package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.Qag
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.GetQagRepositoryImpl.Companion.QAG_CACHE_NAME
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.ImportAutoConfiguration
import org.springframework.boot.autoconfigure.cache.CacheAutoConfiguration
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.CacheManager
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
internal class GetQagRepositoryImplTest {

    @Autowired
    private lateinit var repository: GetQagRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: GetQagDatabaseRepository

    @MockBean
    private lateinit var mapper: QagMapper

    @Autowired
    private lateinit var cacheManager: CacheManager

    private val qagDTO = QagDTO(
        id = UUID.randomUUID(),
        title = "dto-title",
        description = "dto-description",
        postDate = Date(42),
        status = 256,
        username = "dto-username",
        thematiqueId = UUID.randomUUID(),
    )

    private val qag = Qag(
        id = "domain-id",
        thematiqueId = "domain-thematiqueId",
        title = "domain-title",
        description = "domain-description",
        date = Date(14),
        status = QagStatus.MODERATED,
        username = "domain-username",
    )

    @BeforeEach
    fun setUp() {
        cacheManager.getCache(QAG_CACHE_NAME)?.clear()
    }

    @Test
    fun `getQag - when invalid UUID - should return null`() {
        // When
        val result = repository.getQag("invalid UUID")

        // Then
        assertThat(result).isEqualTo(null)
    }

    @Test
    fun `getQag - when not found and no cache - should return null from database`() {
        // Given
        val uuid = UUID.randomUUID()
        given(databaseRepository.getQag(uuid)).willReturn(null)

        // When
        val result = repository.getQag(uuid.toString())

        // Then
        assertThat(result).isEqualTo(null)
        then(databaseRepository).should(only()).getQag(uuid)
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when not found and has cache - should return null from cache`() {
        // Given
        val uuid = UUID.randomUUID()
        given(databaseRepository.getQag(uuid)).willReturn(null)

        // When
        repository.getQag(uuid.toString())
        val result = repository.getQag(uuid.toString())

        // Then
        assertThat(result).isEqualTo(null)
        then(databaseRepository).should(times(1)).getQag(uuid)
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQag - when found and no cache - should return mapped object from database`() {
        // Given
        val uuid = UUID.randomUUID()
        given(databaseRepository.getQag(uuid)).willReturn(qagDTO)
        given(mapper.toDomain(qagDTO)).willReturn(qag)

        // When
        val result = repository.getQag(uuid.toString())

        // Then
        assertThat(result).isEqualTo(qag)
        then(databaseRepository).should(only()).getQag(uuid)
        then(mapper).should(only()).toDomain(qagDTO)
    }

    @Test
    fun `getQag - when found and has cache - should return mapped object from cache`() {
        // Given
        val uuid = UUID.randomUUID()
        given(databaseRepository.getQag(uuid)).willReturn(qagDTO)
        given(mapper.toDomain(qagDTO)).willReturn(qag)

        // When
        repository.getQag(uuid.toString())
        val result = repository.getQag(uuid.toString())

        // Then
        assertThat(result).isEqualTo(qag)
        then(databaseRepository).should(times(1)).getQag(uuid)
        then(mapper).should(times(2)).toDomain(qagDTO)
    }

}