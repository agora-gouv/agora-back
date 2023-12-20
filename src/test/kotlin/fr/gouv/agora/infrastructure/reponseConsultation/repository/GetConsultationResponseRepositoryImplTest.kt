package fr.gouv.agora.infrastructure.reponseConsultation.repository

import fr.gouv.agora.domain.ReponseConsultation
import fr.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.gouv.agora.infrastructure.reponseConsultation.repository.ReponseConsultationCacheRepository.CacheResult
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
internal class GetConsultationResponseRepositoryImplTest {

    @Autowired
    private lateinit var repository: GetConsultationResponseRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: ReponseConsultationDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: ReponseConsultationCacheRepository

    @MockBean
    private lateinit var mapper: ReponseConsultationMapper

    private val reponseConsultation = ReponseConsultation(
        id = "domain-id",
        questionId = "domain-questionId",
        choiceId = "domain-choiceId",
        participationId = "domain-participationId",
        userId = "domain-userId",
        responseText = "domain-responseText"
    )

    private val reponseConsultationDTO = ReponseConsultationDTO(
        id = UUID.randomUUID(),
        consultationId = UUID.randomUUID(),
        questionId = UUID.randomUUID(),
        choiceId = UUID.randomUUID(),
        responseText = "dto-responseText",
        participationId = UUID.randomUUID(),
        participationDate = Date(),
        userId = UUID.randomUUID(),
    )

    @Nested
    @Suppress("DEPRECATION")
    inner class GetConsultationResponsesTestCases {
        @Test
        fun `getConsultationResponses - when invalid UUID - should return emptyList`() {
            // When
            val result = repository.getConsultationResponses("invalid UUID")

            // Then
            assertThat(result).isEqualTo(emptyList<ReponseConsultation>())
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getConsultationResponses - when cache returns CacheReponseConsultationNotFound - should return emptyList`() {
            // Given
            val consultationUUID = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
            given(cacheRepository.getReponseConsultationList(consultationUUID)).willReturn(CacheResult.CacheReponseConsultationNotFound)

            // When
            val result = repository.getConsultationResponses(consultationUUID.toString())

            // Then
            assertThat(result).isEqualTo(emptyList<ReponseConsultation>())
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getConsultationResponses - when returns CacheReponseConsultation - should return mapped dtos`() {
            // Given
            val consultationUUID = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
            given(cacheRepository.getReponseConsultationList(consultationUUID))
                .willReturn(CacheResult.CacheReponseConsultation(listOf(reponseConsultationDTO)))
            given(mapper.toDomain(reponseConsultationDTO)).willReturn(reponseConsultation)

            // When
            val result = repository.getConsultationResponses(consultationUUID.toString())

            // Then
            assertThat(result).isEqualTo(listOf(reponseConsultation))
            then(cacheRepository).should(only()).getReponseConsultationList(consultationUUID)
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).should(only()).toDomain(reponseConsultationDTO)
        }

        @Test
        fun `getConsultationResponses - when returns CacheNotInitialized and databaseRepository returns emptyList - should return emptyList`() {
            // Given
            val consultationUUID = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
            given(cacheRepository.getReponseConsultationList(consultationUUID)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getConsultationResponses(consultationUUID)).willReturn(emptyList())

            // When
            val result = repository.getConsultationResponses(consultationUUID.toString())

            // Then
            assertThat(result).isEqualTo(emptyList<ReponseConsultation>())
            then(cacheRepository).should().getReponseConsultationList(consultationUUID)
            then(cacheRepository).should().insertReponseConsultationList(consultationUUID, emptyList())
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getConsultationResponses(consultationUUID)
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getConsultationResponses - when cache return CacheNotInitialized & databaseRepository returns responses - should insert dtos to cache then return mapped dtos`() {
            // Given
            val consultationUUID = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
            given(cacheRepository.getReponseConsultationList(consultationUUID)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getConsultationResponses(consultationUUID)).willReturn(
                listOf(
                    reponseConsultationDTO
                )
            )
            given(mapper.toDomain(reponseConsultationDTO)).willReturn(reponseConsultation)

            // When
            val result = repository.getConsultationResponses(consultationUUID.toString())

            // Then
            assertThat(result).isEqualTo(listOf(reponseConsultation))
            then(cacheRepository).should().getReponseConsultationList(consultationUUID)
            then(cacheRepository).should()
                .insertReponseConsultationList(consultationUUID, listOf(reponseConsultationDTO))
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getConsultationResponses(consultationUUID)
            then(mapper).should(only()).toDomain(reponseConsultationDTO)
        }
    }

}