package fr.social.gouv.agora.infrastructure.testNotification

import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@Suppress("unused")
class TestController(
    private val qagInfoRepository: QagInfoRepository
) {

    @GetMapping("/test")
    fun testNotification(
        @RequestHeader("Authorization") authorizationHeader: String,
    ): ResponseEntity<*> {
        return ResponseEntity.ok().body(Unit)
    }

}