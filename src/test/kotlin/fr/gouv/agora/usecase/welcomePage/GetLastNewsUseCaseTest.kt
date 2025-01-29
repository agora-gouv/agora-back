package fr.gouv.agora.usecase.welcomePage

import fr.gouv.agora.TestUtils
import fr.gouv.agora.domain.News
import fr.gouv.agora.infrastructure.welcomePage.repository.NewsRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
class GetLastNewsUseCaseTest {

    private lateinit var getLastNewsUseCase: GetLastNewsUseCase

    @Mock
    private lateinit var newsRepository: NewsRepository

    @Mock
    private lateinit var newsJsonMapper: NewsJsonMapper

    private val today = LocalDateTime.of(2024, 12, 25, 12, 0)

    @BeforeEach
    fun setUp() {
        val clock = TestUtils.getFixedClock(today)
        getLastNewsUseCase = GetLastNewsUseCase(newsRepository, clock, newsJsonMapper)
    }

    @Test
    fun `when there is pasts and future news, it returns the last before tomorrow`() {
        // GIVEN
        val todayNews = mock(News::class.java).also {
            given(it.beginDate).willReturn(today)
        }
        val futureNews = mock(News::class.java).also {
            given(it.beginDate).willReturn(today.plusDays(1))
        }
        val pastNews = mock(News::class.java).also {
            given(it.beginDate).willReturn(today.minusDays(1))
        }
        given(newsRepository.getNews()).willReturn(listOf(todayNews, futureNews, pastNews))

        // WHEN
        getLastNewsUseCase.execute()

        // THEN
        then(newsJsonMapper).should().toJson(todayNews)
    }

    @Test
    fun `when there is no news before today, it returns null`() {
        // GIVEN
        val futureNews = mock(News::class.java).also {
            given(it.beginDate).willReturn(today.plusDays(1))
        }
        given(newsRepository.getNews()).willReturn(listOf(futureNews))

        // WHEN
        val actual = getLastNewsUseCase.execute()

        // THEN
        assertThat(actual).isNull()
    }
}
