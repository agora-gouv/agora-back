package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.infrastructure.reponseConsultation.dto.ReponseConsultationDTO
import fr.social.gouv.agora.infrastructure.reponseConsultation.repository.GetReponseConsultationRepositoryImpl.Companion.REPONSE_CONSULTATION_CACHE_NAME
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.EnableCaching
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
@EnableCaching

internal class ReponseConsultationRepositoryImplTest {

    @Autowired
    private lateinit var reponseConsultationRepository: ReponseConsultationRepositoryImpl

    @MockBean
    private lateinit var reponseConsultationDatabaseRepository: ReponseConsultationDatabaseRepository

    @MockBean
    private lateinit var reponseConsultationMapper: ReponseConsultationMapper

    @MockBean
    private lateinit var cacheManager: CacheManager

    @MockBean
    private lateinit var cache: Cache

    @BeforeEach
    fun setUp() {
        given(cacheManager.getCache(REPONSE_CONSULTATION_CACHE_NAME)).willReturn(cache)
    }

    private val reponseConsultationDTO = ReponseConsultationDTO(
        id = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea1733302"),
        consultationId = UUID.randomUUID(),
        questionId = UUID.randomUUID(),
        choiceId = UUID.randomUUID(),
        responseText = "je ne suis pas intéressé",
        participationId = UUID.randomUUID(),
    )

    private val reponseConsultation = ReponseConsultationInserting(
        id = "c29255f2-10ca-4be5-aab1-801ea1733302",
        consultationId = "1234",
        questionId = "1234",
        choiceIds = listOf("1234"),
        responseText = "je ne suis pas intéressé",
        participationId = "1234",
    )

    private val reponseConsultationSansChoice = ReponseConsultationInserting(
        id = "c29255f2-10ca-4be5-aab1-801ea1733302",
        consultationId = "1234",
        questionId = "1234",
        choiceIds = null,
        responseText = "choice est un champs vide",
        participationId = "1234",
    )

    private val reponseConsultationSansChoiceDTO = ReponseConsultationDTO(
        id = UUID.randomUUID(),
        consultationId = UUID.randomUUID(),
        questionId = UUID.randomUUID(),
        choiceId = null,
        responseText = "choice est un champs vide",
        participationId = UUID.randomUUID(),
    )

    @Test
    fun `insertReponseConsultation - when reponseConsultationId is not null - should return INSERT_SUCCESS`() {
        //Given
        given(reponseConsultationDatabaseRepository.save(reponseConsultationDTO)).willReturn(reponseConsultationDTO)
        given(reponseConsultationMapper.toDto(reponseConsultation)).willReturn(listOf(reponseConsultationDTO))

        // When
        val result = reponseConsultationRepository.insertReponseConsultation(reponseConsultation)

        // Then
        assertThat(result).isEqualTo(InsertStatus.INSERT_SUCCESS)
        then(cacheManager).should(only()).getCache(REPONSE_CONSULTATION_CACHE_NAME)
        then(cache).should(only()).evict(reponseConsultation.consultationId)
    }

    @Test
    fun `insertReponseConsultation - when reponseConsultation exist in database - should return INSERT_CONFLICT`() {
        //Given
        given(reponseConsultationMapper.toDto(reponseConsultation)).willReturn(listOf(reponseConsultationDTO))
        given(reponseConsultationDatabaseRepository.existsById(reponseConsultationDTO.id)).willReturn(true)

        // When
        val result = reponseConsultationRepository.insertReponseConsultation(reponseConsultation)

        // Then
        assertThat(result).isEqualTo(InsertStatus.INSERT_CONFLICT)
        then(cacheManager).shouldHaveNoInteractions()
    }

    @Test
    fun `insertReponseConsultation - when choiceIds is null - should return INSERT_SUCCESS`() {
        //Given
        given(reponseConsultationDatabaseRepository.save(reponseConsultationSansChoiceDTO))
            .willReturn(reponseConsultationSansChoiceDTO)
        given(reponseConsultationMapper.toDto(reponseConsultationSansChoice))
            .willReturn(listOf(reponseConsultationSansChoiceDTO))

        // When
        val result = reponseConsultationRepository.insertReponseConsultation(reponseConsultationSansChoice)

        // Then
        assertThat(result).isEqualTo(InsertStatus.INSERT_SUCCESS)
        then(cacheManager).should(only()).getCache(REPONSE_CONSULTATION_CACHE_NAME)
        then(cache).should(only()).evict(reponseConsultation.consultationId)
    }
}