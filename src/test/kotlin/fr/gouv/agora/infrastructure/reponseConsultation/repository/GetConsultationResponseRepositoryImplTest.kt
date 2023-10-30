package fr.gouv.agora.infrastructure.reponseConsultation.repository

import fr.gouv.agora.domain.ReponseConsultation
import fr.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.gouv.agora.infrastructure.reponseConsultation.repository.ReponseConsultationCacheRepository.CacheResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
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
        userId = UUID.randomUUID(),
    )

    @Nested
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

    @Nested
    inner class HasAnsweredConsultationTestCases {

        @AfterEach
        fun tearDown() {
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `hasAnsweredConsultation - when invalid consultation UUID - should return false`() {
            // When
            val result = repository.hasAnsweredConsultation(
                consultationId = "invalid consultationId UUID",
                userId = UUID.randomUUID().toString(),
            )

            // Then
            assertThat(result).isEqualTo(false)
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `hasAnsweredConsultation - when invalid user UUID - should return false`() {
            // When
            val result = repository.hasAnsweredConsultation(
                consultationId = UUID.randomUUID().toString(),
                userId = "invalid useId UUID",
            )

            // Then
            assertThat(result).isEqualTo(false)
            then(cacheRepository).shouldHaveNoInteractions()
            then(databaseRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `hasAnsweredConsultation - when cache returns CacheReponseConsultationNotFound - should return false`() {
            // Given
            val consultationUUID = UUID.randomUUID()
            given(cacheRepository.getReponseConsultationList(consultationUUID)).willReturn(CacheResult.CacheReponseConsultationNotFound)

            // When
            val result = repository.hasAnsweredConsultation(
                consultationId = consultationUUID.toString(),
                userId = UUID.randomUUID().toString(),
            )

            // Then
            assertThat(result).isEqualTo(false)
            then(cacheRepository).should(only()).getReponseConsultationList(consultationId = consultationUUID)
            then(databaseRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `hasAnsweredConsultation - when returns CacheReponseConsultation and has no response from userId - should return false`() {
            // Given
            val consultationUUID = UUID.randomUUID()
            val reponseConsultationDTO = reponseConsultationDTO.copy(
                userId = UUID.fromString("00000000-0000-0000-0000-000000000000"),
            )
            given(cacheRepository.getReponseConsultationList(consultationUUID))
                .willReturn(CacheResult.CacheReponseConsultation(listOf(reponseConsultationDTO)))

            // When
            val result = repository.hasAnsweredConsultation(
                consultationId = consultationUUID.toString(),
                userId = UUID.fromString("abcdef00-1234-5678-9999-abcdefabcdef").toString(),
            )

            // Then
            assertThat(result).isEqualTo(false)
            then(cacheRepository).should(only()).getReponseConsultationList(consultationId = consultationUUID)
            then(databaseRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `hasAnsweredConsultation - when returns CacheReponseConsultation and has response from userId - should return true`() {
            // Given
            val consultationUUID = UUID.randomUUID()
            val userUUID = UUID.randomUUID()
            val reponseConsultationDTO = reponseConsultationDTO.copy(userId = userUUID)
            given(cacheRepository.getReponseConsultationList(consultationUUID))
                .willReturn(CacheResult.CacheReponseConsultation(listOf(reponseConsultationDTO)))

            // When
            val result = repository.hasAnsweredConsultation(
                consultationId = consultationUUID.toString(),
                userId = userUUID.toString(),
            )

            // Then
            assertThat(result).isEqualTo(true)
            then(cacheRepository).should(only()).getReponseConsultationList(consultationId = consultationUUID)
            then(databaseRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `hasAnsweredConsultation - when returns CacheNotInitialized and database has no response from userId - should return false`() {
            // Given
            val consultationUUID = UUID.randomUUID()
            val reponseConsultationDTO = reponseConsultationDTO.copy(
                userId = UUID.fromString("00000000-0000-0000-0000-000000000000"),
            )
            given(cacheRepository.getReponseConsultationList(consultationUUID)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getConsultationResponses(consultationId = consultationUUID))
                .willReturn(listOf(reponseConsultationDTO))

            // When
            val result = repository.hasAnsweredConsultation(
                consultationId = consultationUUID.toString(),
                userId = UUID.fromString("abcdef00-1234-5678-9999-abcdefabcdef").toString(),
            )

            // Then
            assertThat(result).isEqualTo(false)
            then(cacheRepository).should().getReponseConsultationList(consultationId = consultationUUID)
            then(cacheRepository).should().insertReponseConsultationList(
                consultationId = consultationUUID,
                reponseConsultationList = listOf(reponseConsultationDTO),
            )
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getConsultationResponses(consultationId = consultationUUID)
        }

        @Test
        fun `hasAnsweredConsultation - when returns CacheNotInitialized and database has response from userId - should return true`() {
            // Given
            val consultationUUID = UUID.randomUUID()
            val userUUID = UUID.randomUUID()
            val reponseConsultationDTO = reponseConsultationDTO.copy(userId = userUUID)
            given(cacheRepository.getReponseConsultationList(consultationUUID)).willReturn(CacheResult.CacheNotInitialized)
            given(databaseRepository.getConsultationResponses(consultationId = consultationUUID))
                .willReturn(listOf(reponseConsultationDTO))

            // When
            val result = repository.hasAnsweredConsultation(
                consultationId = consultationUUID.toString(),
                userId = userUUID.toString(),
            )

            // Then
            assertThat(result).isEqualTo(true)
            then(cacheRepository).should().getReponseConsultationList(consultationId = consultationUUID)
            then(cacheRepository).should().insertReponseConsultationList(
                consultationId = consultationUUID,
                reponseConsultationList = listOf(reponseConsultationDTO),
            )
            then(cacheRepository).shouldHaveNoMoreInteractions()
            then(databaseRepository).should(only()).getConsultationResponses(consultationId = consultationUUID)
        }
    }

    @Test
    fun `hasAnsweredConsultations - when invalid user UUID - should return emptyMap`() {
        // Given
        val consultationId = UUID.randomUUID()

        // When
        val result = repository.hasAnsweredConsultations(
            consultationIds = listOf(consultationId.toString()),
            userId = "Invalid user UUID",
        )

        // Then
        assertThat(result).isEqualTo(emptyMap<String, Boolean>())
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `hasAnsweredConsultations - when valid user UUID and has only invalid consultation UUIDs - should return emptyMap`() {
        // When
        val result = repository.hasAnsweredConsultations(
            consultationIds = listOf("Invalid consultation UUID"),
            userId = UUID.randomUUID().toString(),
        )

        // Then
        assertThat(result).isEqualTo(emptyMap<String, Boolean>())
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `hasAnsweredConsultations - when valid user UUID and has only valid consultation UUIDs - should return result from database`() {
        // Given
        val userId = UUID.randomUUID()
        val consultationId = UUID.randomUUID()
        given(databaseRepository.getAnsweredConsultations(consultationIDs = listOf(consultationId), userId = userId))
            .willReturn(listOf(consultationId))

        // When
        val result = repository.hasAnsweredConsultations(
            consultationIds = listOf(consultationId.toString()),
            userId = userId.toString(),
        )

        // Then
        assertThat(result).isEqualTo(mapOf(consultationId.toString() to true))
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).should(only())
            .getAnsweredConsultations(consultationIDs = listOf(consultationId), userId = userId)
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `hasAnsweredConsultations - when valid user UUID and has some valid consultation UUIDs - should call database only with valid ones then return result from database with invalid mapped to false`() {
        // Given
        val userId = UUID.randomUUID()
        val consultationId = UUID.randomUUID()
        given(databaseRepository.getAnsweredConsultations(consultationIDs = listOf(consultationId), userId = userId))
            .willReturn(listOf(consultationId))

        // When
        val result = repository.hasAnsweredConsultations(
            consultationIds = listOf(consultationId.toString(), "Invalid consultation UUID"),
            userId = userId.toString(),
        )

        // Then
        assertThat(result).isEqualTo(
            mapOf(
                consultationId.toString() to true,
                "Invalid consultation UUID" to false,
            )
        )
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).should(only())
            .getAnsweredConsultations(consultationIDs = listOf(consultationId), userId = userId)
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getUsersAnsweredConsultation - when invalid consultation UUID - should return emptyList`() {
        // When
        val result = repository.getUsersAnsweredConsultation(consultationId = "Invalid consultation UUID")

        // Then
        assertThat(result).isEqualTo(emptyList<String>())
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getUsersAnsweredConsultation - when valid consultation UUID - should return result from database`() {
        // Given
        val consultationId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        given(databaseRepository.getUsersAnsweredConsultation(consultationId)).willReturn(listOf(userId))

        // When
        val result = repository.getUsersAnsweredConsultation(consultationId = consultationId.toString())

        // Then
        assertThat(result).isEqualTo(listOf(userId.toString()))
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).should(only()).getUsersAnsweredConsultation(consultationId = consultationId)
        then(mapper).shouldHaveNoInteractions()
    }

}