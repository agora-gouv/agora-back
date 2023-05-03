package fr.social.gouv.agora.infrastructure.responseQag.repository

import fr.social.gouv.agora.domain.ResponseQag
import fr.social.gouv.agora.infrastructure.responseQag.dto.ResponseQagDTO
import fr.social.gouv.agora.infrastructure.responseQag.repository.ResponseQagCacheRepository.CacheListResult
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
internal class ResponseQagListRepositoryImplTest {

    @Autowired
    private lateinit var repository: ResponseQagListRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: ResponseQagDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: ResponseQagCacheRepository

    @MockBean
    private lateinit var mapper: ResponseQagMapper

    private val responsaQagDTO = ResponseQagDTO(
        id = UUID.randomUUID(),
        author = "",
        authorPortraitUrl = "",
        authorDescription = "",
        responseDate = Date(0),
        videoUrl = "",
        transcription = "",
        qagId = UUID.randomUUID(),
    )

    private val reponseQag = ResponseQag(
        id = "1234",
        author = "",
        authorPortraitUrl = "",
        authorDescription = "",
        responseDate = Date(0),
        videoUrl = "",
        transcription = "",
        qagId = "4567",
    )

    @Test
    fun `getResponseQagList - when CacheNotInitialized - should call getResponseQagListFromDatabase and insert result (case result = emptylist) to cache`() {
        // Given
        given(cacheRepository.getResponseQagList())
            .willReturn(CacheListResult.CacheNotInitialized)

        given(databaseRepository.getResponseQagList()).willReturn(emptyList())

        // When
        val result = repository.getResponseQagList()

        // Then
        assertThat(result).isEqualTo(emptyList<ResponseQag>())
        then(cacheRepository).should(times(1)).getResponseQagList()
        then(cacheRepository).should(times(1)).insertResponseQagList(emptyList())
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).getResponseQagList()
    }

    @Test
    fun `getResponseQagList - when CacheNotInitialized - should call getResponseQagListFromDatabase and insert result (case result not null) to cache`() {
        // Given
        given(cacheRepository.getResponseQagList())
            .willReturn(CacheListResult.CacheNotInitialized)

        given(databaseRepository.getResponseQagList()).willReturn(listOf(responsaQagDTO))
        given(mapper.toDomain(responsaQagDTO)).willReturn(reponseQag)

        // When
        val result = repository.getResponseQagList()

        // Then
        assertThat(result).isEqualTo(listOf(reponseQag))
        then(cacheRepository).should(times(1)).getResponseQagList()
        then(cacheRepository).should(times(1)).insertResponseQagList(listOf(responsaQagDTO))
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).getResponseQagList()
    }

    @Test
    fun `getResponseQagList - when has cache with emptyList - should return emptylist from cache`() {
        // Given
        given(cacheRepository.getResponseQagList())
            .willReturn(CacheListResult.CachedResponseQagList(emptyList()))

        // When
        val result = repository.getResponseQagList()

        // Then
        assertThat(result).isEqualTo(emptyList<ResponseQag>())
        then(cacheRepository).should(only()).getResponseQagList()
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getResponseQagList - when has cache with listof DTO - should return listof DTO from cache`() {
        // Given
        given(cacheRepository.getResponseQagList())
            .willReturn(CacheListResult.CachedResponseQagList(listOf(responsaQagDTO)))
        given(mapper.toDomain(responsaQagDTO)).willReturn(reponseQag)

        // When
        val result = repository.getResponseQagList()

        // Then
        assertThat(result).isEqualTo(listOf(reponseQag))
        then(cacheRepository).should(only()).getResponseQagList()
        then(databaseRepository).shouldHaveNoInteractions()
    }
}