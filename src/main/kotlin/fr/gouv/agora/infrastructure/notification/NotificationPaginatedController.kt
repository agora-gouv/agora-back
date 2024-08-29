package fr.gouv.agora.infrastructure.notification

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.usecase.notification.NotificationPaginatedUseCase
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Notifications")
class NotificationPaginatedController(
    private val notificationPaginatedUseCase: NotificationPaginatedUseCase,
    private val notificationPaginatedJsonMapper: NotificationPaginatedJsonMapper,
    private val authentificationHelper: AuthentificationHelper,
) {

    @GetMapping("/notifications/paginated/{pageNumber}")
    fun getNotificationPaginatedList(
        @PathVariable pageNumber: String,
    ): ResponseEntity<*> {
        val userId = authentificationHelper.getUserId()!!
        val pageNumberInt = pageNumber.toIntOrNull() ?: return ResponseEntity.badRequest().body(Unit)
        val notifications = notificationPaginatedUseCase
            .getNotificationPaginated(userId = userId, pageNumber = pageNumberInt)
            ?: return ResponseEntity.badRequest().body(Unit)

        return ResponseEntity.ok(notificationPaginatedJsonMapper.toJson(notifications))
    }

}
