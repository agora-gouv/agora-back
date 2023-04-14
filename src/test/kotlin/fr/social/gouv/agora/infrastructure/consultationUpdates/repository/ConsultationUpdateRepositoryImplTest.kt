package fr.social.gouv.agora.infrastructure.consultationUpdates.repository

import fr.social.gouv.agora.domain.ConsultationStatus
import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateDTO
import fr.social.gouv.agora.infrastructure.consultationUpdates.repository.ConsultationUpdateRepositoryImpl.Companion.CONSULTATION_UPDATE_CACHE
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
internal class ConsultationUpdateRepositoryImplTest {

    @Autowired
    private lateinit var repository: ConsultationUpdateRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: ConsultationUpdateDatabaseRepository

    @MockBean
    private lateinit var mapper: ConsultationUpdateMapper

    @Autowired
    private lateinit var cacheManager: CacheManager

    private val consultationUpdateDTO = ConsultationUpdateDTO(
        id = UUID.randomUUID(),
        step = 42,
        description = "dto-description",
        consultationId = UUID.randomUUID(),
    )

    private val consultationUpdate = ConsultationUpdate(
        status = ConsultationStatus.COLLECTING_DATA,
        description = "domain-description",
    )

    @BeforeEach
    fun setUp() {
        cacheManager.getCache(CONSULTATION_UPDATE_CACHE)?.clear()
    }

    @Test
    fun `getConsultationUpdate - when invalid UUID - should return null`() {
        // When
        val result = repository.getConsultationUpdate("invalidUUID")

        // Then
        assertThat(result).isEqualTo(null)
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationUpdate - when not found and no cache - should return null`() {
        // Given
        val uuid = UUID.randomUUID()
        given(databaseRepository.getLastConsultationUpdate(uuid)).willReturn(null)

        // When
        val result = repository.getConsultationUpdate(uuid.toString())

        // Then
        assertThat(result).isEqualTo(null)
        then(databaseRepository).should(only()).getLastConsultationUpdate(uuid)
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationUpdate - when not found with cache - should return null`() {
        // Given
        val uuid = UUID.randomUUID()
        given(databaseRepository.getLastConsultationUpdate(uuid)).willReturn(null)

        // When
        repository.getConsultationUpdate(uuid.toString())
        val result = repository.getConsultationUpdate(uuid.toString())

        // Then
        assertThat(result).isEqualTo(null)
        then(databaseRepository).should(times(1)).getLastConsultationUpdate(uuid)
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getConsultationUpdate - when found and no cache - should return dto parsed from database`() {
        // Given
        val uuid = UUID.randomUUID()
        given(databaseRepository.getLastConsultationUpdate(uuid)).willReturn(consultationUpdateDTO)
        given(mapper.toDomain(consultationUpdateDTO)).willReturn(consultationUpdate)

        // When
        val result = repository.getConsultationUpdate(uuid.toString())

        // Then
        assertThat(result).isEqualTo(consultationUpdate)
        then(databaseRepository).should(only()).getLastConsultationUpdate(uuid)
        then(mapper).should(only()).toDomain(consultationUpdateDTO)
    }

    @Test
    fun `getConsultationUpdate - when found with cache - should return dto parsed from cache`() {
        // Given
        val uuid = UUID.randomUUID()
        given(databaseRepository.getLastConsultationUpdate(uuid)).willReturn(consultationUpdateDTO)
        given(mapper.toDomain(consultationUpdateDTO)).willReturn(consultationUpdate)

        // When
        repository.getConsultationUpdate(uuid.toString())
        val result = repository.getConsultationUpdate(uuid.toString())

        // Then
        assertThat(result).isEqualTo(consultationUpdate)
        then(databaseRepository).should(times(1)).getLastConsultationUpdate(uuid)
        then(mapper).should(times(2)).toDomain(consultationUpdateDTO)
    }

}