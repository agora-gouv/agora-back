package fr.gouv.agora.infrastructure.profile.repository

import fr.gouv.agora.infrastructure.profile.dto.DemographicInfoAskDateDTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.time.LocalDate
import java.time.Month
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class DemographicInfoAskDateRepositoryImplTest {

    @Autowired
    private lateinit var repository: DemographicInfoAskDateRepositoryImpl

    @MockBean
    private lateinit var cacheRepository: DemographicInfoAskDateCacheRepository

    @MockBean
    private lateinit var databaseRepository: DemographicInfoAskDateDatabaseRepository

    @MockBean
    private lateinit var mapper: DemographicInfoAskDateMapper

    private val userId = UUID.randomUUID()
    private val askLocalDate = LocalDate.of(2023, Month.SEPTEMBER, 28)

    @Test
    fun `getDate - when invalid user UUID - should return null`() {
        // When
        val result = repository.getDate(userId = "Invalid user UUID")

        // Then
        assertThat(result).isNull()
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getDate - when cache is initialized - should return result from cache`() {
        // Given
        given(cacheRepository.getDate(userId = userId)).willReturn(askLocalDate)

        // When
        val result = repository.getDate(userId = userId.toString())

        // Then
        assertThat(result).isEqualTo(askLocalDate)
        then(cacheRepository).should(only()).getDate(userId = userId)
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `getDate - when is not initialized - should get data from database, insert to cache then return date`() {
        // Given
        given(cacheRepository.getDate(userId = userId)).willReturn(null)
        val demographicAskDateDTO = mock(DemographicInfoAskDateDTO::class.java).also {
            given(it.askDate).willReturn(askLocalDate.toDate())
        }
        given(databaseRepository.getAskDate(userId = userId)).willReturn(demographicAskDateDTO)

        given(mapper.toDate(dto = demographicAskDateDTO)).willReturn(askLocalDate)

        // When
        val result = repository.getDate(userId = userId.toString())

        // Then
        assertThat(result).isEqualTo(askLocalDate)
        then(cacheRepository).should().getDate(userId = userId)
        then(cacheRepository).should().insertDate(userId = userId, askDate = askLocalDate)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(only()).getAskDate(userId = userId)
        then(mapper).should(only()).toDate(demographicAskDateDTO)
    }

    @Test
    fun `insertDate - when invalid UUID - should do nothing`() {
        // When
        repository.insertDate(userId = "Invalid user UUID")

        // Then
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `insertDate - when invalid user UUID - should do nothing`() {
        // When
        repository.insertDate(userId = "Invalid user UUID")

        // Then
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `insertDate - when valid user UUID - should insert to database and cache`() {
        // Given
        val demographicInfoAskDateDTO = mock(DemographicInfoAskDateDTO::class.java)
        given(mapper.toDto(userId = userId)).willReturn(demographicInfoAskDateDTO)

        val savedDemographicInfoAskDateDTO = mock(DemographicInfoAskDateDTO::class.java).also {
            given(it.userId).willReturn(userId)
        }
        given(databaseRepository.save(demographicInfoAskDateDTO)).willReturn(savedDemographicInfoAskDateDTO)
        given(mapper.toDate(dto = savedDemographicInfoAskDateDTO)).willReturn(askLocalDate)

        // When
        repository.insertDate(userId = userId.toString())

        // Then
        then(cacheRepository).should(only()).insertDate(userId = userId, askDate = askLocalDate)
        then(databaseRepository).should(only()).save(demographicInfoAskDateDTO)
        then(mapper).should().toDto(userId = userId)
        then(mapper).should().toDate(dto = savedDemographicInfoAskDateDTO)
        then(mapper).shouldHaveNoMoreInteractions()
    }

    @Test
    fun `deleteDate - when invalid user UUID - should do nothing`() {
        // When
        repository.deleteDate(userId = "Invalid user UUID")

        // Then
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
        then(mapper).shouldHaveNoInteractions()
    }

    @Test
    fun `deleteDate - when valid user UUID - should delete from and cache`() {
        // When
        repository.deleteDate(userId = userId.toString())

        // Then
        then(cacheRepository).should(only()).deleteDate(userId = userId)
        then(databaseRepository).should(only()).deleteAskDate(userId = userId)
        then(mapper).shouldHaveNoInteractions()
    }

}