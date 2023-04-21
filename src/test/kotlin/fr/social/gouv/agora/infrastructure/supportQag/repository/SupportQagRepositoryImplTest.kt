package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.assertj.core.api.Assertions.assertThat
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
internal class SupportQagRepositoryImplTest {

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

    private val supportQagDTO = SupportQagDTO(
        id = UUID.randomUUID(),
        userId = "12345",
        qagId = UUID.fromString("f3a26670-df6e-11ed-b5ea-0242ac120002"),
    )

    @Test
    fun `insertSupportQag - when mapper returns null - should return FAILURE`() {
        // Given
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
    fun `insertSupportQag - when mapper returns DTO and ID exist - should return SUCCESS and save with another DTO`() {
        // Given
        val supportQagDTO1 = supportQagDTO.copy(id = UUID.randomUUID())
        val supportQagDTO2 = supportQagDTO.copy(id = UUID.randomUUID())
        given(mapper.toDto(supportQagInserting)).willReturn(supportQagDTO1, supportQagDTO2)
        given(databaseRepository.existsById(supportQagDTO1.id)).willReturn(true)
        given(databaseRepository.existsById(supportQagDTO2.id)).willReturn(false)

        // When
        val result = repository.insertSupportQag(supportQagInserting)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.SUCCESS)
        then(mapper).should(times(2)).toDto(supportQagInserting)
        inOrder(databaseRepository).also {
            then(databaseRepository).should(it, times(1)).existsById(supportQagDTO1.id)
            then(databaseRepository).should(it, times(1)).existsById(supportQagDTO2.id)
            then(databaseRepository).should(it).save(supportQagDTO2)
            it.verifyNoMoreInteractions()
        }
        then(supportQagCacheRepository).should(only())
            .insertSupportQag(supportQagDTO2.qagId, supportQagDTO2.userId, supportQagDTO2)
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
        then(mapper).should(times(3)).toDto(supportQagInserting)
        inOrder(databaseRepository).also {
            then(databaseRepository).should(it, times(3)).existsById(supportQagDTO.id)
            it.verifyNoMoreInteractions()
        }
        then(supportQagCacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `deleteSupportQag - when invalid UUID for qagID - should return FAILURE`() {
        // Given
        val supportQagDeletingInvalidUUID = SupportQagDeleting(
            userId = "12345",
            qagId = "45678",
        )
        // When
        val result = repository.deleteSupportQag(supportQagDeletingInvalidUUID)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.FAILURE)
        then(databaseRepository).shouldHaveNoInteractions()
        then(supportQagCacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `deleteSupportQag - when valid UUID for qagID AND exists in Database should return SUCCESS`() {
        // Given
        val supportQagDeletingValidUUID = SupportQagDeleting(
            userId = "12345",
            qagId = "a2dd3d9a-df92-11ed-b5ea-0242ac120002",
        )
        given(
            databaseRepository.deleteSupportQag(
                supportQagDeletingValidUUID.userId,
                UUID.fromString(supportQagDeletingValidUUID.qagId)
            )
        ).willReturn(1)
        // When
        val result = repository.deleteSupportQag(supportQagDeletingValidUUID)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.SUCCESS)
        then(databaseRepository).should(times(1))
            .deleteSupportQag(supportQagDeletingValidUUID.userId, UUID.fromString(supportQagDeletingValidUUID.qagId))
        then(supportQagCacheRepository).should(only()).insertSupportQag(
            UUID.fromString(supportQagDeletingValidUUID.qagId),
            supportQagDeletingValidUUID.userId,
            null
        )
    }

    @Test
    fun `deleteSupportQag - when valid UUID for qagID AND NOT exist in Database should return FAILURE`() {
        // Given
        val supportQagDeletingValidUUID = SupportQagDeleting(
            userId = "12345",
            qagId = "a2dd3d9a-df92-11ed-b5ea-0242ac120002",
        )
        given(
            databaseRepository.deleteSupportQag(
                supportQagDeletingValidUUID.userId,
                UUID.fromString(supportQagDeletingValidUUID.qagId)
            )
        ).willReturn(0)
        // When
        val result = repository.deleteSupportQag(supportQagDeletingValidUUID)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.FAILURE)
        then(databaseRepository).should(times(1))
            .deleteSupportQag(supportQagDeletingValidUUID.userId, UUID.fromString(supportQagDeletingValidUUID.qagId))
        then(supportQagCacheRepository).shouldHaveNoInteractions()
    }

}