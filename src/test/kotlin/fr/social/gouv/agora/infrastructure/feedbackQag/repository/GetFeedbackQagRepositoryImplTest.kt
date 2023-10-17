package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQag
import fr.social.gouv.agora.domain.FeedbackQagInserting
import fr.social.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.*
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
internal class GetFeedbackQagRepositoryImplTest {

    @Autowired
    private lateinit var repository: GetFeedbackQagRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: FeedbackQagDatabaseRepository

    @MockBean
    private lateinit var mapper: FeedbackQagMapper

    private val feedbackQagDTO = FeedbackQagDTO(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID(),
        qagId = UUID.randomUUID(),
        isHelpful = 0,
    )

    @Nested
    inner class GetFeedbackQagListTestCases {
        @Test
        fun `getFeedbackQagList - when invalid qagId - should return emptyList() without doing anything`() {
            // When
            val result = repository.getFeedbackQagList(
                qagId = "invalid qagId UUID",
            )

            // Then
            assertThat(result).isEqualTo(emptyList<FeedbackQagInserting>())
            then(databaseRepository).shouldHaveNoInteractions()
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getFeedbackQagList - when valid qagId and database return emptyList() - should return emptyList()`() {
            // Given
            val qagUUID = UUID.randomUUID()
            given(databaseRepository.getFeedbackQagList(qagId = qagUUID)).willReturn(emptyList())

            // When
            val result = repository.getFeedbackQagList(
                qagId = qagUUID.toString(),
            )

            // Then
            assertThat(result).isEqualTo(emptyList<FeedbackQag>())
            then(databaseRepository).should(only()).getFeedbackQagList(qagId = qagUUID)
            then(mapper).shouldHaveNoInteractions()
        }

        @Test
        fun `getFeedbackQagList - when valid qagId and database return a DTO - should return a list of mapped DTO`() {
            // Given
            val qagUUID = UUID.randomUUID()
            given(databaseRepository.getFeedbackQagList(qagId = qagUUID)).willReturn(listOf(feedbackQagDTO))
            val feedbackQag = mock(FeedbackQag::class.java)
            given(mapper.toDomain(feedbackQagDTO)).willReturn(feedbackQag)

            // When
            val result = repository.getFeedbackQagList(
                qagId = qagUUID.toString(),
            )

            // Then
            assertThat(result).isEqualTo(listOf(feedbackQag))
            then(databaseRepository).should(only()).getFeedbackQagList(qagId = qagUUID)
            then(mapper).should(only()).toDomain(feedbackQagDTO)
        }
    }
}