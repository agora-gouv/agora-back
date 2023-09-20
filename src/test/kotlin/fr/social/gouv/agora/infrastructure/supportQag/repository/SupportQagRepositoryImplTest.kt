package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQagDeleting
import fr.social.gouv.agora.domain.SupportQagInserting
import fr.social.gouv.agora.infrastructure.supportQag.dto.SupportQagDTO
import fr.social.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.assertj.core.api.Assertions.assertThat
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

    @Nested
    inner class InsertSupportQagTestCases {
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
            then(cacheRepository).should(only()).insertSupportQag(savedSupportQagDTO)
        }

        @Test
        fun `insertSupportQag - when mapper returns DTO but insert causes exception - should initialize cache then return SUCCESS`() {
            // Given
            given(mapper.toDto(supportQagInserting)).willReturn(supportQagDTO)

            val qagId = UUID.randomUUID()
            val userId = UUID.randomUUID()
            val savedSupportQagDTO = mock(SupportQagDTO::class.java).also {
                given(it.qagId).willReturn(qagId)
                given(it.userId).willReturn(userId)
            }
            given(databaseRepository.save(supportQagDTO)).willReturn(savedSupportQagDTO)

            given(cacheRepository.insertSupportQag(savedSupportQagDTO)).willThrow(IllegalStateException::class.java)
            val storedSupportQagDTO = mock(SupportQagDTO::class.java)
            given(databaseRepository.getAllSupportQagList()).willReturn(listOf(storedSupportQagDTO))

            // When
            val result = repository.insertSupportQag(supportQagInserting)

            // Then
            assertThat(result).isEqualTo(SupportQagResult.SUCCESS)
            then(databaseRepository).should().save(supportQagDTO)
            then(databaseRepository).should().getAllSupportQagList()
            then(databaseRepository).shouldHaveNoMoreInteractions()
            then(cacheRepository).should().insertSupportQag(savedSupportQagDTO)
            then(cacheRepository).should().initializeCache(listOf(storedSupportQagDTO))
            then(cacheRepository).shouldHaveNoMoreInteractions()
        }
    }

    @Nested
    inner class DeleteSupportQagTestCases {
        @Test
        fun `deleteSupportQag - when invalid UUID for qagID - should return FAILURE`() {
            // When
            val result = repository.deleteSupportQag(
                SupportQagDeleting(
                    qagId = "invalid qagId UUID",
                    userId = UUID.randomUUID().toString(),
                )
            )

            // Then
            assertThat(result).isEqualTo(SupportQagResult.FAILURE)
            then(databaseRepository).shouldHaveNoInteractions()
            then(cacheRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `deleteSupportQag - when invalid UUID for userId - should return FAILURE`() {
            // When
            val result = repository.deleteSupportQag(
                SupportQagDeleting(
                    qagId = UUID.randomUUID().toString(),
                    userId = "invalid userId UUID",
                )
            )

            // Then
            assertThat(result).isEqualTo(SupportQagResult.FAILURE)
            then(databaseRepository).shouldHaveNoInteractions()
            then(cacheRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `deleteSupportQag - when valid UUID for qagID AND exists in Database - should return SUCCESS`() {
            // Given
            val qagId = UUID.randomUUID()
            val userId = UUID.randomUUID()
            val supportQagDeletingValidUUID = SupportQagDeleting(
                qagId = qagId.toString(),
                userId = userId.toString(),
            )
            given(databaseRepository.deleteSupportQag(userId = userId, qagId = qagId)).willReturn(1)

            // When
            val result = repository.deleteSupportQag(supportQagDeletingValidUUID)

            // Then
            assertThat(result).isEqualTo(SupportQagResult.SUCCESS)
            then(databaseRepository).should(only()).deleteSupportQag(userId = userId, qagId = qagId)
            then(cacheRepository).should(only()).deleteSupportQag(qagId = qagId, userId = userId)
        }

        @Test
        fun `deleteSupportQag - when valid UUID for qagID AND exists in Database but delete fails - should initialize cache then return SUCCESS`() {
            // Given
            val qagId = UUID.randomUUID()
            val userId = UUID.randomUUID()
            val supportQagDeletingValidUUID = SupportQagDeleting(
                qagId = qagId.toString(),
                userId = userId.toString(),
            )
            given(databaseRepository.deleteSupportQag(userId = userId, qagId = qagId)).willReturn(1)

            given(cacheRepository.deleteSupportQag(qagId = qagId, userId = userId))
                .willThrow(IllegalStateException::class.java)
            val storedSupportQagDTO = mock(SupportQagDTO::class.java)
            given(databaseRepository.getAllSupportQagList()).willReturn(listOf(storedSupportQagDTO))

            // When
            val result = repository.deleteSupportQag(supportQagDeletingValidUUID)

            // Then
            assertThat(result).isEqualTo(SupportQagResult.SUCCESS)
            then(databaseRepository).should().deleteSupportQag(userId = userId, qagId = qagId)
            then(databaseRepository).should().getAllSupportQagList()
            then(databaseRepository).shouldHaveNoMoreInteractions()
            then(cacheRepository).should().deleteSupportQag(qagId = qagId, userId = userId)
            then(cacheRepository).should().initializeCache(listOf(storedSupportQagDTO))
            then(cacheRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `deleteSupportQag - when valid UUID for qagID AND NOT exist in Database should return FAILURE`() {
            // Given
            val qagId = UUID.randomUUID()
            val userId = UUID.randomUUID()
            val supportQagDeletingValidUUID = SupportQagDeleting(
                qagId = qagId.toString(),
                userId = userId.toString(),
            )
            given(databaseRepository.deleteSupportQag(userId = userId, qagId = qagId)).willReturn(0)

            // When
            val result = repository.deleteSupportQag(supportQagDeletingValidUUID)

            // Then
            assertThat(result).isEqualTo(SupportQagResult.FAILURE)
            then(databaseRepository).should(only()).deleteSupportQag(userId = userId, qagId = qagId)
            then(cacheRepository).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class DeleteSupportListByQagIdTestCases {
        @Test
        fun `deleteSupportListByQagId - when invalid UUID for qagID - should return FAILURE`() {
            // When
            val result = repository.deleteSupportListByQagId(
                qagId = "invalid qagId UUID",
            )

            // Then
            assertThat(result).isEqualTo(SupportQagResult.FAILURE)
            then(databaseRepository).shouldHaveNoInteractions()
            then(cacheRepository).shouldHaveNoInteractions()
        }

        @Test
        fun `deleteSupportListByQagId - when valid UUID for qagID AND exists in Database - should return SUCCESS`() {
            // Given
            val qagId = UUID.randomUUID()
            given(databaseRepository.deleteSupportListByQagId(qagId = qagId)).willReturn(1)

            // When
            val result = repository.deleteSupportListByQagId(qagId = qagId.toString())

            // Then
            assertThat(result).isEqualTo(SupportQagResult.SUCCESS)
            then(databaseRepository).should(only()).deleteSupportListByQagId(qagId = qagId)
            then(cacheRepository).should(only()).deleteSupportListByQagId(qagId = qagId)
        }

        @Test
        fun `deleteSupportListByQagId - when valid UUID for qagID AND exists in Database but delete fails in cache - should initialize cache then return SUCCESS`() {
            // Given
            val qagId = UUID.randomUUID()
            given(databaseRepository.deleteSupportListByQagId(qagId = qagId)).willReturn(1)

            given(cacheRepository.deleteSupportListByQagId(qagId = qagId))
                .willThrow(IllegalStateException::class.java)
            val storedSupportQagDTO = mock(SupportQagDTO::class.java)
            given(databaseRepository.getAllSupportQagList()).willReturn(listOf(storedSupportQagDTO))

            // When
            val result = repository.deleteSupportListByQagId(qagId = qagId.toString())

            // Then
            assertThat(result).isEqualTo(SupportQagResult.SUCCESS)
            then(databaseRepository).should().deleteSupportListByQagId(qagId = qagId)
            then(databaseRepository).should().getAllSupportQagList()
            then(databaseRepository).shouldHaveNoMoreInteractions()
            then(cacheRepository).should().deleteSupportListByQagId(qagId = qagId)
            then(cacheRepository).should().initializeCache(listOf(storedSupportQagDTO))
            then(cacheRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `deleteSupportListByQagId - when valid UUID for qagID AND NOT exist in Database should return FAILURE`() {
            // Given
            val qagId = UUID.randomUUID()
            given(databaseRepository.deleteSupportListByQagId(qagId = qagId)).willReturn(0)

            // When
            val result = repository.deleteSupportListByQagId(qagId = qagId.toString())

            // Then
            assertThat(result).isEqualTo(SupportQagResult.FAILURE)
            then(databaseRepository).should(only()).deleteSupportListByQagId(qagId = qagId)
            then(cacheRepository).shouldHaveNoInteractions()
        }
    }
}