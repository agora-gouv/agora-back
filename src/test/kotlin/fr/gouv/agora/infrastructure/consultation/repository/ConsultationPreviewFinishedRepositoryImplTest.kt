package fr.gouv.agora.infrastructure.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewFinishedInfo
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.gouv.agora.infrastructure.consultation.repository.ConsultationFinishedCacheRepository.CacheResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class ConsultationPreviewFinishedRepositoryImplTest {

    @Autowired
    private lateinit var repository: ConsultationPreviewFinishedRepositoryImpl

    @MockBean
    private lateinit var cacheRepository: ConsultationFinishedCacheRepository

    @MockBean
    private lateinit var databaseRepository: ConsultationDatabaseRepository

    @MockBean
    private lateinit var mapper: ConsultationPreviewFinishedInfoMapper

    @Test
    fun `getConsultationFinishedList - when cache not initialized - should insert result from database to cache then return mapped result`() {
        // Given
        given(cacheRepository.getConsultationFinishedList()).willReturn(CacheResult.CacheNotInitialized)
        given(databaseRepository.getConsultationFinishedList()).willReturn(emptyList())

        // When
        val result = repository.getConsultationFinishedList()

        // Then
        assertThat(result).isEqualTo(emptyList<ConsultationPreviewFinishedInfo>())
        inOrder(cacheRepository, databaseRepository).also {
            then(cacheRepository).should(it).getConsultationFinishedList()
            then(databaseRepository).should(it).getConsultationFinishedList()
            then(cacheRepository).should(it).insertConsultationFinishedList(emptyList())
            it.verifyNoMoreInteractions()
        }
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationFinishedList - when has cache - should return mapped dto`() {
        // Given
        val consultationDTO = mock(ConsultationDTO::class.java)
        given(cacheRepository.getConsultationFinishedList())
            .willReturn(CacheResult.CachedConsultationFinishedList(listOf(consultationDTO)))

        val consultationPreviewFinishedInfo = mock(ConsultationPreviewFinishedInfo::class.java)
        given(mapper.toDomain(consultationDTO)).willReturn(consultationPreviewFinishedInfo)

        // When
        val result = repository.getConsultationFinishedList()

        // Then
        assertThat(result).isEqualTo(listOf(consultationPreviewFinishedInfo))
        then(cacheRepository).should(only()).getConsultationFinishedList()
        then(mapper).should(only()).toDomain(consultationDTO)
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `clearCache - should only clear cache`() {
        // When
        repository.clearCache()

        // Then
        then(cacheRepository).should(only()).clearCache()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

}