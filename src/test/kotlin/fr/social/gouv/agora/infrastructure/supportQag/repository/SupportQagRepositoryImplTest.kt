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
    private lateinit var cacheRepository: SupportQagCacheRepository

    private val supportQagInserting = mock(SupportQagInserting::class.java)
    private val supportQagDTO = mock(SupportQagDTO::class.java)

    @Test
    fun `insertSupportQag - when mapper returns null - should return FAILURE`() {
        // Given
        given(mapper.toDto(supportQagInserting)).willReturn(null)

        // When
        val result = repository.insertSupportQag(supportQagInserting)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.FAILURE)
        then(databaseRepository).shouldHaveNoInteractions()
        then(cacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertSupportQag - when mapper returns DTO - should return SUCCESS`() {
        // Given
        given(mapper.toDto(supportQagInserting)).willReturn(supportQagDTO)

        val qagId = UUID.randomUUID()
        val userId = UUID.randomUUID()
        val savedSupportQagDTO = mock(SupportQagDTO::class.java).also {
            given(it.qagId).willReturn(qagId)
            given(it.userId).willReturn(userId)
        }
        given(databaseRepository.save(supportQagDTO)).willReturn(savedSupportQagDTO)

        // When
        val result = repository.insertSupportQag(supportQagInserting)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.SUCCESS)
        then(databaseRepository).should(only()).save(supportQagDTO)
        then(cacheRepository).should(only()).insertSupportQag(qagId, userId, savedSupportQagDTO)
    }

    @Test
    fun `deleteSupportQag - when invalid UUID for qagID - should return FAILURE`() {
        // Given
        val supportQagDeletingInvalidUUID = SupportQagDeleting(
            qagId = "45678",
            userId = "12345",
        )
        // When
        val result = repository.deleteSupportQag(supportQagDeletingInvalidUUID)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.FAILURE)
        then(databaseRepository).shouldHaveNoInteractions()
        then(cacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `deleteSupportQag - when valid UUID for qagID AND exists in Database should return SUCCESS`() {
        // Given
        val supportQagDeletingValidUUID = SupportQagDeleting(
            qagId = "a2dd3d9a-df92-11ed-b5ea-0242ac120002",
            userId = "bc9e81be-eb4d-11ed-a05b-0242ac120003",
        )
        given(
            databaseRepository.deleteSupportQag(
                UUID.fromString(supportQagDeletingValidUUID.userId),
                UUID.fromString(supportQagDeletingValidUUID.qagId)
            )
        ).willReturn(1)
        // When
        val result = repository.deleteSupportQag(supportQagDeletingValidUUID)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.SUCCESS)
        then(databaseRepository).should(times(1))
            .deleteSupportQag(
                UUID.fromString(supportQagDeletingValidUUID.userId),
                UUID.fromString(supportQagDeletingValidUUID.qagId)
            )
        then(cacheRepository).should(only()).insertSupportQag(
            UUID.fromString(supportQagDeletingValidUUID.qagId),
            UUID.fromString(supportQagDeletingValidUUID.userId),
            null
        )
    }

    @Test
    fun `deleteSupportQag - when valid UUID for qagID AND NOT exist in Database should return FAILURE`() {
        // Given
        val supportQagDeletingValidUUID = SupportQagDeleting(
            qagId = "a2dd3d9a-df92-11ed-b5ea-0242ac120002",
            userId = "bc9e81be-eb4d-11ed-a05b-0242ac120003",
        )
        given(
            databaseRepository.deleteSupportQag(
                UUID.fromString(supportQagDeletingValidUUID.userId),
                UUID.fromString(supportQagDeletingValidUUID.qagId)
            )
        ).willReturn(0)
        // When
        val result = repository.deleteSupportQag(supportQagDeletingValidUUID)

        // Then
        assertThat(result).isEqualTo(SupportQagResult.FAILURE)
        then(databaseRepository).should(times(1))
            .deleteSupportQag(
                UUID.fromString(supportQagDeletingValidUUID.userId),
                UUID.fromString(supportQagDeletingValidUUID.qagId)
            )
        then(cacheRepository).shouldHaveNoInteractions()
    }

}