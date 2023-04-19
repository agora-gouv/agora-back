package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import fr.social.gouv.agora.infrastructure.supportQag.repository.SupportQagCacheRepository.CacheResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
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
internal class GetSupportQagRepositoryImplTest {

    @Autowired
    private lateinit var repository: GetSupportQagRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: SupportQagDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: SupportQagCacheRepository

    @MockBean
    private lateinit var mapper: SupportQagMapper

    private val supportQagDTO = SupportQagDTO(
        id = UUID.randomUUID(),
        userId = "userId",
        qagId = UUID.randomUUID(),
    )

    private val supportQag = mock(SupportQag::class.java)

    @Test
    fun `getSupportQag - when invalid qagId - should return null`() {
        // When
        val result = repository.getSupportQag(
            qagId = "invalid qagId UUID",
            userId = "userId",
        )

        // Then
        assertThat(result).isNull()
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getSupportQag - when CacheNotInitialized - should return supportQag first, then supportCount from database and insert result to cache`() {
        // Given
        val qagUUID = UUID.randomUUID()
        given(cacheRepository.getSupportCount(qagUUID)).willReturn(null)
        given(cacheRepository.getSupportQag(qagId = qagUUID, userId = "userId"))
            .willReturn(CacheResult.CacheNotInitialized)

        given(databaseRepository.getSupportCount(qagUUID)).willReturn(50)
        given(databaseRepository.getSupportQag(qagId = qagUUID, userId = "userId")).willReturn(null)

        given(mapper.toDomain(supportCount = 50, dto = null)).willReturn(supportQag)

        // When
        val result = repository.getSupportQag(
            qagId = qagUUID.toString(),
            userId = "userId",
        )

        // Then
        assertThat(result).isEqualTo(supportQag)
        inOrder(cacheRepository).also {
            then(cacheRepository).should(it).getSupportQag(qagId = qagUUID, userId = "userId")
            then(cacheRepository).should(it).insertSupportQag(qagId = qagUUID, userId = "userId", supportQagDTO = null)
            then(cacheRepository).should(it).getSupportCount(qagUUID)
            then(cacheRepository).should(it).insertSupportCount(qagId = qagUUID, supportCount = 50)
            it.verifyNoMoreInteractions()
        }
        then(databaseRepository).should(times(1)).getSupportCount(qagUUID)
        then(databaseRepository).should(times(1)).getSupportQag(qagId = qagUUID, userId = "userId")
        then(mapper).should(only()).toDomain(supportCount = 50, dto = null)
    }

    @Test
    fun `getSupportQag - when CachedSupportQagNotFound - should return mapped object from cache`() {
        // Given
        val qagUUID = UUID.randomUUID()
        given(cacheRepository.getSupportCount(qagUUID)).willReturn(13)
        given(cacheRepository.getSupportQag(qagId = qagUUID, userId = "userId"))
            .willReturn(CacheResult.CachedSupportQagNotFound)

        given(mapper.toDomain(supportCount = 13, dto = null)).willReturn(supportQag)

        // When
        val result = repository.getSupportQag(
            qagId = qagUUID.toString(),
            userId = "userId",
        )

        // Then
        assertThat(result).isEqualTo(supportQag)
        then(cacheRepository).should(times(1)).getSupportCount(qagUUID)
        then(cacheRepository).should(times(1)).getSupportQag(qagId = qagUUID, userId = "userId")
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).should(only()).toDomain(supportCount = 13, dto = null)
    }

    @Test
    fun `getSupportQag - when CachedSupportQag - should return mapped object from cache`() {
        // Given
        val qagUUID = UUID.randomUUID()
        given(cacheRepository.getSupportCount(qagUUID)).willReturn(42)
        given(cacheRepository.getSupportQag(qagId = qagUUID, userId = "userId"))
            .willReturn(CacheResult.CachedSupportQag(supportQagDTO))

        given(mapper.toDomain(supportCount = 42, dto = supportQagDTO)).willReturn(supportQag)

        // When
        val result = repository.getSupportQag(
            qagId = qagUUID.toString(),
            userId = "userId",
        )

        // Then
        assertThat(result).isEqualTo(supportQag)
        then(cacheRepository).should(times(1)).getSupportCount(qagUUID)
        then(cacheRepository).should(times(1)).getSupportQag(qagId = qagUUID, userId = "userId")
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).should(only()).toDomain(supportCount = 42, dto = supportQagDTO)
    }

}