package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.Mockito.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.util.*

@ExtendWith(SpringExtension::class)
@SpringBootTest
internal class SupportQagInsertingRepositoryImplTest {

    @Autowired
    private lateinit var repository: SupportQagRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: SupportQagDatabaseRepository

    @MockBean
    private lateinit var mapper: SupportQagMapper

    @MockBean
    private lateinit var supportQagCacheRepository: SupportQagCacheRepository

    private val supportQagInserting = SupportQagInserting(
        userId = "12345",
        qagId = "f3a26670-df6e-11ed-b5ea-0242ac120002",
    )
    private val supportQagDeletingInvalidUUID = SupportQagDeleting(
        userId = "12345",
        qagId = "45678",
    )
    private val supportQagDeletingvalidUUID = SupportQagDeleting(
        userId = "12345",
        qagId = "a2dd3d9a-df92-11ed-b5ea-0242ac120002",
    )
    private val supportQagDTO = SupportQagDTO(
        id = UUID.randomUUID(),
        userId = "12345",
        qagId = UUID.fromString("f3a26670-df6e-11ed-b5ea-0242ac120002"),
    )

    @Test
    fun `insertSupportQag - when mapper returns null - should return FAILURE`() {
        // Given
        val supportQagInserting = mock(SupportQagInserting::class.java)
        given(mapper.toDto(supportQagInserting)).willReturn(null)

        // When
        val result = repository.insertSupportQag(supportQagInserting)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.FAILURE)
        then(databaseRepository).shouldHaveNoInteractions()
        then(supportQagCacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertSupportQag - when mapper returns DTO and ID does not exist - should return SUCCESS`() {
        // Given
        given(mapper.toDto(supportQagInserting)).willReturn(supportQagDTO)
        given(databaseRepository.existsById(supportQagDTO.id)).willReturn(false)

        // When
        val result = repository.insertSupportQag(supportQagInserting)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.SUCCESS)
        then(databaseRepository).should(times(1)).save(supportQagDTO)
        then(supportQagCacheRepository).should(only())
            .insertSupportQag(supportQagDTO.qagId, supportQagDTO.userId, supportQagDTO)
    }

    @Test
    fun `insertSupportQag - when mapper returns DTO and ID exist - should return SUCCESS and create with another DTO`() {
        // Given
        given(mapper.toDto(supportQagInserting)).willReturn(supportQagDTO)
        given(databaseRepository.existsById(supportQagDTO.id)).willReturn(true, false)

        // When
        val result = repository.insertSupportQag(supportQagInserting)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.SUCCESS)
        then(mapper).should(times(2)).toDto(supportQagInserting)
        inOrder(databaseRepository).also {
            then(databaseRepository).should(it, times(2)).existsById(supportQagDTO.id)
            then(databaseRepository).should(it).save(supportQagDTO)
            it.verifyNoMoreInteractions()
        }
        then(supportQagCacheRepository).should(only())
            .insertSupportQag(supportQagDTO.qagId, supportQagDTO.userId, supportQagDTO)
    }

    @Test
    fun `insertSupportQag - when mapper returns DTO and ID exist 3 times - should return FAILURE`() {
        // Given
        given(mapper.toDto(supportQagInserting)).willReturn(supportQagDTO)
        given(databaseRepository.existsById(supportQagDTO.id)).willReturn(true, true, true)

        // When
        val result = repository.insertSupportQag(supportQagInserting)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.FAILURE)
        then(mapper).should(times(4)).toDto(supportQagInserting)
        inOrder(databaseRepository).also {
            then(databaseRepository).should(it, times(3)).existsById(supportQagDTO.id)
            it.verifyNoMoreInteractions()
        }
        then(supportQagCacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `deleteSupportQag - when invalid UUID for qagID - should return FAILURE`() {
        // Given

        // When
        val result = repository.deleteSupportQag(supportQagDeletingInvalidUUID)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.FAILURE)
        then(databaseRepository).shouldHaveNoInteractions()
        then(supportQagCacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `deleteSupportQag - when valid UUID for qagID should return SUCCESS`() {
        // Given

        // When
        val result = repository.deleteSupportQag(supportQagDeletingvalidUUID)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.SUCCESS)
        then(databaseRepository).should(times(1))
            .deleteSupportQag(supportQagDeletingvalidUUID.userId, UUID.fromString(supportQagDeletingvalidUUID.qagId))
        then(supportQagCacheRepository).should(only()).insertSupportQag(
            UUID.fromString(
                supportQagDeletingvalidUUID.qagId
            ),
            supportQagDeletingvalidUUID.userId,
            null
        )
    }

}