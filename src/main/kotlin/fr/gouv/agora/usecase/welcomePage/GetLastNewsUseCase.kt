package fr.gouv.agora.usecase.welcomePage

import fr.gouv.agora.infrastructure.welcomePage.NewsJson
import fr.gouv.agora.infrastructure.welcomePage.repository.NewsDatabaseRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDate

@Service
class GetLastNewsUseCase(
    private val newsDatabaseRepository: NewsDatabaseRepository,
    private val clock: Clock,
    private val newsJsonMapper: NewsJsonMapper,
) {
    fun execute(): NewsJson? {
        val allNews = newsDatabaseRepository.getNewsList()

        val lastNewsBeforeToday = allNews
            .filter { it.beginDate <= LocalDate.now(clock) }
            .maxByOrNull { it.beginDate }
            ?: return null

        return newsJsonMapper.toJson(lastNewsBeforeToday)
    }
}
