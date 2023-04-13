package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation
import fr.social.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.social.gouv.agora.infrastructure.reponseConsultation.repository.GetReponseConsultationRepositoryImpl.Companion.REPONSE_CONSULTATION_CACHE_NAME
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
internal class GetReponseConsultationRepositoryImplTest {

    @Autowired
    private lateinit var repository: GetReponseConsultationRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: ReponseConsultationDatabaseRepository

    @MockBean
    private lateinit var reponseConsultationMapper: ReponseConsultationMapper

    @Autowired
    @Suppress("unused")
    private lateinit var cacheManager: CacheManager

    @BeforeEach
    fun setUp() {
        cacheManager.getCache(REPONSE_CONSULTATION_CACHE_NAME)?.clear()
    }

    private val reponseConsultation = ReponseConsultation(
        id = "domain-id",
        questionId = "domain-questionId",
        choiceId = "domain-choiceId",
        participationId = "domain-participationId",
    )

    private val reponseConsultationDTO = ReponseConsultationDTO(
        id = UUID.randomUUID(),
        consultationId = UUID.randomUUID(),
        questionId = UUID.randomUUID(),
        choiceId = UUID.randomUUID(),
        responseText = "dto-responseText",
        participationId = UUID.randomUUID(),
    )

    @Test
    fun `getConsultationResponses - when invalid UUID - should return emptyList`() {
        // When
        val result = repository.getConsultationResponses("invalid UUID")

        // Then
        assertThat(result).isEqualTo(emptyList<ReponseConsultation>())
        then(databaseRepository).shouldHaveNoInteractions()
        then(reponseConsultationMapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationResponses - when databaseRepository returns emptyList and no cache - should return emptyList`() {
        // Given
        val consultationUUID = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(databaseRepository.getConsultationResponses(consultationUUID)).willReturn(emptyList())

        // When
        val result = repository.getConsultationResponses(consultationUUID.toString())

        // Then
        assertThat(result).isEqualTo(emptyList<ReponseConsultation>())
        then(databaseRepository).should(only()).getConsultationResponses(consultationUUID)
        then(reponseConsultationMapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationResponses - when databaseRepository returns emptyList and has cache - should return emptyList`() {
        // Given
        val consultationUUID = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(databaseRepository.getConsultationResponses(consultationUUID)).willReturn(emptyList())

        // When
        repository.getConsultationResponses(consultationUUID.toString())
        val result = repository.getConsultationResponses(consultationUUID.toString())

        // Then
        assertThat(result).isEqualTo(emptyList<ReponseConsultation>())
        then(databaseRepository).should(times(1)).getConsultationResponses(consultationUUID)
        then(reponseConsultationMapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationResponses - when databaseRepository returns responses and no cache - should return mapped list of reponseConsultation from database`() {
        // Given
        val consultationUUID = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(databaseRepository.getConsultationResponses(consultationUUID)).willReturn(listOf(reponseConsultationDTO))
        given(reponseConsultationMapper.toDomain(reponseConsultationDTO)).willReturn(reponseConsultation)

        // When
        val result = repository.getConsultationResponses(consultationUUID.toString())

        // Then
        assertThat(result).isEqualTo(listOf(reponseConsultation))
        then(databaseRepository).should(only()).getConsultationResponses(consultationUUID)
        then(reponseConsultationMapper).should(only()).toDomain(reponseConsultationDTO)
    }

    @Test
    fun `getConsultationResponses - when databaseRepository returns responses and has cache - should return mapped list of reponseConsultation from cache`() {
        // Given
        val consultationUUID = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(databaseRepository.getConsultationResponses(consultationUUID)).willReturn(listOf(reponseConsultationDTO))
        given(reponseConsultationMapper.toDomain(reponseConsultationDTO)).willReturn(reponseConsultation)

        // When
        repository.getConsultationResponses(consultationUUID.toString())
        val result = repository.getConsultationResponses(consultationUUID.toString())

        // Then
        assertThat(result).isEqualTo(listOf(reponseConsultation))
        then(databaseRepository).should(times(1)).getConsultationResponses(consultationUUID)
        then(reponseConsultationMapper).should(times(2)).toDomain(reponseConsultationDTO)
    }

}