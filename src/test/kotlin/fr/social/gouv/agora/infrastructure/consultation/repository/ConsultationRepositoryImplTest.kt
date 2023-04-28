package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.Consultation
import fr.social.gouv.agora.infrastructure.consultation.dto.ConsultationDTO
import fr.social.gouv.agora.infrastructure.consultation.repository.ConsultationRepositoryImpl.Companion.CONSULTATION_CACHE_NAME
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
    classes = [
        CacheAutoConfiguration::class,
        RedisAutoConfiguration::class,
    ]
)
internal class ConsultationRepositoryImplTest {

    @Autowired
    private lateinit var repository: ConsultationRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: ConsultationDatabaseRepository

    @MockBean
    private lateinit var consultationMapper: ConsultationMapper

    @Autowired
    @Suppress("unused")
    private lateinit var cacheManager: CacheManager

    @BeforeEach
    fun setUp() {
        cacheManager.getCache(CONSULTATION_CACHE_NAME)?.clear()
    }

    private val consultation = Consultation(
        id = "domain-id",
        title = "domain-title",
        coverUrl = "domain-cover_url",
        abstract = "domain-abstract",
        startDate = Date(0),
        endDate = Date(1),
        questionCount = "domain-question_count",
        estimatedTime = "domain-estimated_time",
        participantCountGoal = 6401,
        description = "domain-description",
        tipsDescription = "domain-tips_description",
        thematiqueId = "domain-id_thematique",
    )

    private val consultationDto = ConsultationDTO(
        id = UUID.randomUUID(),
        title = "dto-title",
        abstract = "dto-abstract",
        startDate = Date(0),
        endDate = Date(1),
        coverUrl = "dto-cover_url",
        questionCount = "dto-question_count",
        estimatedTime = "dto-estimated_time",
        participantCountGoal = 6401,
        description = "dto-description",
        tipsDescription = "dto-tips_description",
        thematiqueId = UUID.randomUUID(),
    )

    @Test
    fun `getConsultation - when invalid UUID - should return null`() {
        // When
        val result = repository.getConsultation("lol")

        // Then
        assertThat(result).isEqualTo(null)
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultation - when not found and has no cache - should return null`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(databaseRepository.getConsultation(uuid)).willReturn(null)

        // When
        val result = repository.getConsultation(uuid.toString())

        // Then
        assertThat(result).isEqualTo(null)
        then(databaseRepository).should(only()).getConsultation(uuid)
    }

    @Test
    fun `getConsultation - when not found and has cache - should return null`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(databaseRepository.getConsultation(uuid)).willReturn(null)

        // When
        repository.getConsultation(uuid.toString())
        val result = repository.getConsultation(uuid.toString())

        // Then
        assertThat(result).isEqualTo(null)
        then(databaseRepository).should(times(1)).getConsultation(uuid)
    }

    @Test
    fun `getConsultation - when found and has no cache - should return parsed dto from databaseRepository`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(databaseRepository.getConsultation(uuid)).willReturn(consultationDto)
        given(consultationMapper.toDomain(consultationDto)).willReturn(consultation)

        // When
        val result = repository.getConsultation(uuid.toString())

        // Then
        assertThat(result).isEqualTo(consultation)
        then(databaseRepository).should(only()).getConsultation(uuid)
    }

    @Test
    fun `getConsultation - when found and has cache - should return parsed dto from cache`() {
        // Given
        val uuid = UUID.fromString("c29255f2-10ca-4be5-aab1-801ea173337c")
        given(databaseRepository.getConsultation(uuid)).willReturn(consultationDto)
        given(consultationMapper.toDomain(consultationDto)).willReturn(consultation)

        // When
        repository.getConsultation(uuid.toString())
        val result = repository.getConsultation(uuid.toString())

        // Then
        assertThat(result).isEqualTo(consultation)
        then(databaseRepository).should(times(1)).getConsultation(uuid)
    }

}