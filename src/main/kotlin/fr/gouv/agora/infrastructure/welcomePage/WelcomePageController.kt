package fr.gouv.agora.infrastructure.welcomePage

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/welcome_page")
class WelcomePageController(
) {
    @GetMapping("/last_news")
    fun getThematiqueList(): ResponseEntity<LastNewsJson> {
        return ResponseEntity.ok()
            .body(LastNewsJson("", "", "", ""))
    }
}
