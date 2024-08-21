package fr.gouv.agora.usecase.welcomePage

import fr.gouv.agora.infrastructure.welcomePage.NewsJson
import fr.gouv.agora.infrastructure.welcomePage.repository.NewsDatabaseRepository
import fr.gouv.agora.infrastructure.welcomePage.repository.NewsRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime

@Service
class GetLastNewsUseCase(
    private val newsRepository: NewsRepository,
    private val clock: Clock,
    private val newsJsonMapper: NewsJsonMapper,
) {
    fun execute(): NewsJson? {
        val allNews = newsRepository.getNews()

        val lastNewsBeforeToday = allNews
            .filter { it.beginDate <= LocalDateTime.now(clock) }
            .maxByOrNull { it.beginDate }
            ?: return null

        return newsJsonMapper.toJson(lastNewsBeforeToday)
    }
}
