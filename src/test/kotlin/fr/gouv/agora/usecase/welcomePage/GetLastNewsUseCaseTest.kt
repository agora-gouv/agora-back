package fr.gouv.agora.usecase.welcomePage

import fr.gouv.agora.TestUtils
import fr.gouv.agora.infrastructure.welcomePage.repository.NewsDTO
import fr.gouv.agora.infrastructure.welcomePage.repository.NewsDatabaseRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Clock
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class GetLastNewsUseCaseTest {

    private lateinit var getLastNewsUseCase: GetLastNewsUseCase

    @Mock
    private lateinit var newsDatabaseRepository: NewsDatabaseRepository

    @Mock
    private lateinit var newsJsonMapper: NewsJsonMapper

    val today = LocalDate.of(2024, 12, 25)

    @BeforeEach
    fun setUp() {
        val clock = TestUtils.getFixedClock(today.atStartOfDay())
        getLastNewsUseCase = GetLastNewsUseCase(newsDatabaseRepository, clock, newsJsonMapper)
    }

    @Test
    fun `when there is pasts and future news, it returns the last before tomorrow`() {
        // GIVEN
        val todayNews = mock(NewsDTO::class.java).also {
            given(it.beginDate).willReturn(today)
        }
        val futureNews = mock(NewsDTO::class.java).also {
            given(it.beginDate).willReturn(today.plusDays(1))
        }
        val pastNews = mock(NewsDTO::class.java).also {
            given(it.beginDate).willReturn(today.minusDays(1))
        }
        given(newsDatabaseRepository.getNewsList()).willReturn(listOf(todayNews, futureNews, pastNews))

        // WHEN
        val actual = getLastNewsUseCase.execute()

        // THEN
        then(newsJsonMapper).should().toJson(todayNews)
    }
}
