package fr.social.gouv.agora.infrastructure.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackQagStatus
import fr.social.gouv.agora.infrastructure.feedbackQag.dto.FeedbackQagDTO
import fr.social.gouv.agora.infrastructure.feedbackQag.repository.FeedbackQagCacheRepository.CacheResult
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
internal class GetFeedbackQagRepositoryImplTest {

    @Autowired
    private lateinit var repository: GetFeedbackQagRepositoryImpl

    @MockBean
    private lateinit var databaseRepository: FeedbackQagDatabaseRepository

    @MockBean
    private lateinit var cacheRepository: FeedbackQagCacheRepository

    private val feedbackQagDTO = FeedbackQagDTO(
        id = UUID.randomUUID(),
        userId = UUID.randomUUID(),
        qagId = UUID.randomUUID(),
        isHelpful = 0,
    )
    private val feedbackQagStatusFalse = FeedbackQagStatus(isExist = false)
    private val feedbackQagStatusTrue = FeedbackQagStatus(isExist = true)

    @Test
    fun `getFeedbackQag - when invalid qagId - should return null without doing anything`() {
        // When
        val result = repository.getFeedbackQagStatus(
            qagId = "invalid qagId UUID",
            userId = UUID.randomUUID().toString(),
        )

        // Then
        assertThat(result).isNull()
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getFeedbackQag - when invalid userId - should return null without doing anything`() {
        // When
        val result = repository.getFeedbackQagStatus(
            qagId = UUID.randomUUID().toString(),
            userId = "invalid userId",
        )

        // Then
        assertThat(result).isNull()
        then(cacheRepository).shouldHaveNoInteractions()
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getFeedbackQag - when CacheNotInitialized - should call getFeedbackQag from database and insert result to cache`() {
        // Given
        val qagUUID = UUID.randomUUID()
        val userUUID = UUID.randomUUID()
        given(cacheRepository.getFeedbackQag(qagId = qagUUID, userId = userUUID))
            .willReturn(CacheResult.CacheNotInitialized)

        given(databaseRepository.getFeedbackQag(qagId = qagUUID, userId = userUUID)).willReturn(null)

        // When
        val result = repository.getFeedbackQagStatus(
            qagId = qagUUID.toString(),
            userId = userUUID.toString(),
        )

        // Then
        assertThat(result).isEqualTo(feedbackQagStatusFalse)
        then(cacheRepository).should().getFeedbackQag(qagId = qagUUID, userId = userUUID)
        then(cacheRepository).should().insertFeedbackQag(qagId = qagUUID, userId = userUUID, feedbackQagDTO = null)
        then(cacheRepository).shouldHaveNoMoreInteractions()
        then(databaseRepository).should(times(1)).getFeedbackQag(qagId = qagUUID, userId = userUUID)
    }

    @Test
    fun `getFeedbackQag - when CachedFeedbackQagNotFound - should return object from cache`() {
        // Given
        val qagUUID = UUID.randomUUID()
        val userUUID = UUID.randomUUID()
        given(cacheRepository.getFeedbackQag(qagId = qagUUID, userId = userUUID))
            .willReturn(CacheResult.CachedFeedbackQagNotFound)

        // When
        val result = repository.getFeedbackQagStatus(
            qagId = qagUUID.toString(),
            userId = userUUID.toString(),
        )

        // Then
        assertThat(result).isEqualTo(feedbackQagStatusFalse)
        then(cacheRepository).should(only()).getFeedbackQag(qagId = qagUUID, userId = userUUID)
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getFeedbackQag - when CachedFeedbackQag - should return FeedbackQagDTO from cache`() {
        // Given
        val qagUUID = UUID.randomUUID()
        val userUUID = UUID.randomUUID()
        given(cacheRepository.getFeedbackQag(qagId = qagUUID, userId = userUUID))
            .willReturn(CacheResult.CachedFeedbackQag(feedbackQagDTO))

        // When
        val result = repository.getFeedbackQagStatus(
            qagId = qagUUID.toString(),
            userId = userUUID.toString(),
        )

        // Then
        assertThat(result).isEqualTo(feedbackQagStatusTrue)
        then(cacheRepository).should(only()).getFeedbackQag(qagId = qagUUID, userId = userUUID)
        then(databaseRepository).shouldHaveNoInteractions()
    }

}