package fr.gouv.agora.infrastructure.welcomePage

import fr.gouv.agora.usecase.welcomePage.GetLastNewsUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/welcome_page")
class WelcomePageController(
    val getLastNewsUseCase: GetLastNewsUseCase,
) {
    @GetMapping("/last_news")
    fun getLastNews(): ResponseEntity<NewsJson> {
        val lastNews = getLastNewsUseCase.execute()
            ?: return ResponseEntity.notFound().build()

        return ResponseEntity.ok().body(lastNews)
    }
}
