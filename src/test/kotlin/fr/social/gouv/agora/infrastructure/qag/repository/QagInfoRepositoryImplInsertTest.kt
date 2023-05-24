package fr.social.gouv.agora.infrastructure.qag.repository

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.infrastructure.qag.dto.QagDTO
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInsertionResult
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
internal class QagInfoRepositoryImplInsertTest {

    @Autowired
    private lateinit var repository: QagInfoRepositoryImpl

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
        postDate = Date(10),
        status = 1,
        username = "username",
        thematiqueId = UUID.randomUUID(),
    )

    private val qagInfo = QagInfo(
        id = "id",
        thematiqueId = "thematiqueId",
        title = "title",
        description = "description",
        date = Date(20),
        status = QagStatus.MODERATED,
        username = "username",
    )

    @Test
    fun `insertQagInfo - when mapper returns null - should return FAILURE`() {
        given(mapper.toDto(qagInfo)).willReturn(null)

        // When
        val result = repository.insertQagInfo(qagInfo)

        // Then
        assertThat(result).isEqualTo(QagInsertionResult.FAILURE)
        then(databaseRepository).shouldHaveNoInteractions()
        then(cacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertQagInfo - when mapper returns DTO and ID does not exist - should return SUCCESS`() {
        // Given
        given(mapper.toDto(qagInfo)).willReturn(qagDTO)
        given(databaseRepository.existsById(qagDTO.id)).willReturn(false)

        // When
        val result = repository.insertQagInfo(qagInfo)

        // Then
        assertThat(result).isEqualTo(QagInsertionResult.SUCCESS)
        then(databaseRepository).should(times(1)).save(qagDTO)
        then(cacheRepository).should(only())
            .insertQag(qagUUID = qagDTO.id, qagDTO = qagDTO)
    }

    @Test
    fun `insertQagInfo - when mapper returns DTO and ID exist - should return SUCCESS and save with another DTO`() {
        // Given
        val qagDTO1 = qagDTO.copy(id = UUID.randomUUID())
        val qagDTO2 = qagDTO.copy(id = UUID.randomUUID())
        given(mapper.toDto(qagInfo)).willReturn(qagDTO1, qagDTO2)
        given(databaseRepository.existsById(qagDTO1.id)).willReturn(true)
        given(databaseRepository.existsById(qagDTO2.id)).willReturn(false)

        // When
        val result = repository.insertQagInfo(qagInfo)

        // Then
        assertThat(result).isEqualTo(QagInsertionResult.SUCCESS)
        then(mapper).should(times(2)).toDto(qagInfo)
        inOrder(databaseRepository).also {
            then(databaseRepository).should(it, times(1)).existsById(qagDTO1.id)
            then(databaseRepository).should(it, times(1)).existsById(qagDTO2.id)
            then(databaseRepository).should(it).save(qagDTO2)
            it.verifyNoMoreInteractions()
        }
        then(cacheRepository).should(only())
            .insertQag(qagUUID = qagDTO2.id, qagDTO = qagDTO2)
    }

    @Test
    fun `insertQagInfo - when mapper returns DTO and ID exist 3 times - should return FAILURE`() {
        // Given
        given(mapper.toDto(qagInfo)).willReturn(qagDTO)
        given(databaseRepository.existsById(qagDTO.id)).willReturn(true, true, true)

        // When
        val result = repository.insertQagInfo(qagInfo)

        // Then
        assertThat(result).isEqualTo(QagInsertionResult.FAILURE)
        then(mapper).should(times(3)).toDto(qagInfo)
        inOrder(databaseRepository).also {
            then(databaseRepository).should(it, times(3)).existsById(qagDTO.id)
            it.verifyNoMoreInteractions()
        }
        then(cacheRepository).shouldHaveNoInteractions()
    }
}