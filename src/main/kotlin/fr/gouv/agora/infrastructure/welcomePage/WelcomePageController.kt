package fr.gouv.agora.infrastructure.welcomePage

import fr.gouv.agora.infrastructure.welcomePage.repository.NewsDatabaseRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/welcome_page")
class WelcomePageController(
    val newsDatabaseRepository: NewsDatabaseRepository
) {
    @GetMapping("/last_news")
    fun getThematiqueList(): ResponseEntity<NewsJson> {
        val lastNews = newsDatabaseRepository
            .getNewsList()
            .filter { it.beginDate <= LocalDate.now() }
            .maxBy { it.beginDate }

        return ResponseEntity.ok()
            .body(
                NewsJson(
                    lastNews.description,
                    lastNews.callToActionText,
                    lastNews.routeName,
                    lastNews.routeArgument,
                )
            )
    }
}
