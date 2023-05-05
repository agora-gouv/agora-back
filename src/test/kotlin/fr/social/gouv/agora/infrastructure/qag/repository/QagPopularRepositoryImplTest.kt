package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.QagInfo
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.infrastructure.qag.repository.QagCacheRepository.CachePopularListResult
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
    fun `getQagPopularList - when cache is not initialized - should call getQagPopularListFromDatabase and insert result (case result = emptylist) to cache`() {
        // Given
        given(cacheRepository.getQagPopularList())
            .willReturn(CachePopularListResult.CacheNotInitialized)

        given(databaseRepository.getQagPopularList()).willReturn(emptyList())

        // When
        val result = repository.getQagPopularList()

        // Then
        assertThat(result).isEqualTo(emptyList<QagInfo>())
        then(cacheRepository).should(times(1)).getQagPopularList()
        then(cacheRepository).should(times(1)).insertQagPopularList(emptyList())
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).getQagPopularList()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagPopularList - when cache is not initialized - should call getQagPopularListFromDatabase and insert result (case result not null) to cache`() {
        // Given
        given(cacheRepository.getQagPopularList())
            .willReturn(CachePopularListResult.CacheNotInitialized)

        given(databaseRepository.getQagPopularList()).willReturn(listOf(qagDTO))
        given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

        // When
        val result = repository.getQagPopularList()

        // Then
        assertThat(result).isEqualTo(listOf(qagInfo))
        then(cacheRepository).should(times(1)).getQagPopularList()
        then(cacheRepository).should(times(1)).insertQagPopularList(listOf(qagDTO))
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).getQagPopularList()
        then(mapper).should(only()).toDomain(qagDTO)
    }

    @Test
    fun `getQagPopularList - when has cache with emptyList - should return emptylist from cache`() {
        // Given
        given(cacheRepository.getQagPopularList())
            .willReturn(CachePopularListResult.CachedQagList(emptyList()))

        // When
        val result = repository.getQagPopularList()

        // Then
        assertThat(result).isEqualTo(emptyList<QagInfo>())
        then(cacheRepository).should(only()).getQagPopularList()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getQagPopularList - when has cache with listof DTO - should return listof DTO from cache`() {
        // Given
        given(cacheRepository.getQagPopularList())
            .willReturn(CachePopularListResult.CachedQagList(listOf(qagDTO)))
        given(mapper.toDomain(qagDTO)).willReturn(qagInfo)

        // When
        val result = repository.getQagPopularList()

        // Then
        assertThat(result).isEqualTo(listOf(qagInfo))
        then(cacheRepository).should(only()).getQagPopularList()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).should(only()).toDomain(qagDTO)
    }
}