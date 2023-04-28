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
        userId = "userId",
        qagId = UUID.randomUUID(),
        isHelpful = 0,
    )
    private val feedbackQagStatusFalse = FeedbackQagStatus(isExist = false)
    private val feedbackQagStatusTrue = FeedbackQagStatus(isExist = true)

    @Test
    fun `getFeedbackQag - when invalid qagId - should return null`() {
        // When
        val result = repository.getFeedbackQagStatus(
            qagId = "invalid qagId UUID",
            userId = "userId",
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
        given(cacheRepository.getFeedbackQag(qagId = qagUUID, userId = "userId"))
            .willReturn(CacheResult.CacheNotInitialized)

        given(databaseRepository.getFeedbackQag(qagId = qagUUID, userId = "userId")).willReturn(null)

        // When
        val result = repository.getFeedbackQagStatus(
            qagId = qagUUID.toString(),
            userId = "userId",
        )

        // Then
        assertThat(result).isEqualTo(feedbackQagStatusFalse)
        inOrder(cacheRepository).also {
            then(cacheRepository).should(it).getFeedbackQag(qagId = qagUUID, userId = "userId")
            then(cacheRepository).should(it)
                .insertFeedbackQag(qagId = qagUUID, userId = "userId", feedbackQagDTO = null)
            it.verifyNoMoreInteractions()
        }
        then(databaseRepository).should(times(1)).getFeedbackQag(qagId = qagUUID, userId = "userId")
    }

    @Test
    fun `getFeedbackQag - when CachedFeedbackQagNotFound - should return object from cache`() {
        // Given
        val qagUUID = UUID.randomUUID()
        given(cacheRepository.getFeedbackQag(qagId = qagUUID, userId = "userId"))
            .willReturn(CacheResult.CachedFeedbackQagNotFound)

        // When
        val result = repository.getFeedbackQagStatus(
            qagId = qagUUID.toString(),
            userId = "userId",
        )

        // Then
        assertThat(result).isEqualTo(feedbackQagStatusFalse)
        then(cacheRepository).should(only()).getFeedbackQag(qagId = qagUUID, userId = "userId")
        then(databaseRepository).shouldHaveNoInteractions()
    }

    @Test
    fun `getFeedbackQag - when CachedFeedbackQag - should return FeedbackQagDTO from cache`() {
        // Given
        val qagUUID = UUID.randomUUID()
        given(cacheRepository.getFeedbackQag(qagId = qagUUID, userId = "userId"))
            .willReturn(CacheResult.CachedFeedbackQag(feedbackQagDTO))

        // When
        val result = repository.getFeedbackQagStatus(
            qagId = qagUUID.toString(),
            userId = "userId",
        )

        // Then
        assertThat(result).isEqualTo(feedbackQagStatusTrue)
        then(cacheRepository).should(only()).getFeedbackQag(qagId = qagUUID, userId = "userId")
        then(databaseRepository).shouldHaveNoInteractions()
    }

}