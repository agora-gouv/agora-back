package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQagInserting
import fr.social.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
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
internal class FeedbackQagRepositoryImplTest {
    @Autowired
    private lateinit var repository: FeedbackQagRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: FeedbackQagDatabaseRepository

    @MockBean
    private lateinit var mapper: FeedbackQagMapper

    @MockBean
    private lateinit var feedbackQagCacheRepository: FeedbackQagCacheRepository

    private val feedbackQag = FeedbackQagInserting(
        qagId = "1f066238-e29b-11ed-b5ea-0242ac120002",
        userId = "1234",
        isHelpful = true,
    )

    private val feedbackQagDTO = FeedbackQagDTO(
        id = UUID.randomUUID(),
        userId = "1234",
        qagId = UUID.fromString("1f066238-e29b-11ed-b5ea-0242ac120002"),
        isHelpful = 1,
    )

    @Test
    fun `insertFeedbackQag - when mapper returns null - should return FAILURE`() {
        // Given
        given(mapper.toDto(feedbackQag)).willReturn(null)

        // When
        val result = repository.insertFeedbackQag(feedbackQag)

        // Then
        assertThat(result).isEqualTo(FeedbackQagResult.FAILURE)
        then(databaseRepository).shouldHaveNoInteractions()
        then(feedbackQagCacheRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `insertFeedbackQag - when mapper returns DTO and ID does not exist - should return SUCCESS`() {
        // Given
        given(mapper.toDto(feedbackQag)).willReturn(feedbackQagDTO)
        given(databaseRepository.existsById(feedbackQagDTO.id)).willReturn(false)

        // When
        val result = repository.insertFeedbackQag(feedbackQag)

        // Then
        assertThat(result).isEqualTo(FeedbackQagResult.SUCCESS)
        then(databaseRepository).should(times(1)).save(feedbackQagDTO)
        then(feedbackQagCacheRepository).should(only())
            .insertFeedbackQag(feedbackQagDTO.qagId, feedbackQagDTO.userId, feedbackQagDTO)
    }

    @Test
    fun `insertFeedbackQag - when mapper returns DTO and ID exist - should return SUCCESS and save with another DTO`() {
        // Given
        val feedbackQagDTO1 = feedbackQagDTO.copy(id = UUID.randomUUID())
        val feedbackQagDTO2 = feedbackQagDTO.copy(id = UUID.randomUUID())
        given(mapper.toDto(feedbackQag)).willReturn(feedbackQagDTO1, feedbackQagDTO2)
        given(databaseRepository.existsById(feedbackQagDTO1.id)).willReturn(true)
        given(databaseRepository.existsById(feedbackQagDTO2.id)).willReturn(false)

        // When
        val result = repository.insertFeedbackQag(feedbackQag)

        // Then
        assertThat(result).isEqualTo(FeedbackQagResult.SUCCESS)
        then(mapper).should(times(2)).toDto(feedbackQag)
        inOrder(databaseRepository).also {
            then(databaseRepository).should(it, times(1)).existsById(feedbackQagDTO1.id)
            then(databaseRepository).should(it, times(1)).existsById(feedbackQagDTO2.id)
            then(databaseRepository).should(it).save(feedbackQagDTO2)
            it.verifyNoMoreInteractions()
        }
        then(feedbackQagCacheRepository).should(only())
            .insertFeedbackQag(feedbackQagDTO2.qagId, feedbackQagDTO2.userId, feedbackQagDTO2)
    }

    @Test
    fun `insertFeedbackQag - when mapper returns DTO and ID exist 3 times - should return FAILURE`() {
        // Given
        given(mapper.toDto(feedbackQag)).willReturn(feedbackQagDTO)
        given(databaseRepository.existsById(feedbackQagDTO.id)).willReturn(true, true, true)

        // When
        val result = repository.insertFeedbackQag(feedbackQag)

        // Then
        assertThat(result).isEqualTo(FeedbackQagResult.FAILURE)
        then(mapper).should(times(3)).toDto(feedbackQag)
        inOrder(databaseRepository).also {
            then(databaseRepository).should(it, times(3)).existsById(feedbackQagDTO.id)
            it.verifyNoMoreInteractions()
        }
        then(feedbackQagCacheRepository).shouldHaveNoInteractions()
    }
}