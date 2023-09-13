package fr.social.gouv.agora.infrastructure.notification

import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.notification.NotificationPaginatedUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class NotificationPaginatedController(
    private val notificationPaginatedUseCase: NotificationPaginatedUseCase,
    private val notificationPaginatedJsonMapper: NotificationPaginatedJsonMapper,
) {

    @GetMapping("/notifications/paginated/{pageNumber}")
    fun getNotificationList(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable pageNumber: String,
    ): ResponseEntity<*> {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val usedPageNumber = pageNumber.toIntOrNull()

        return usedPageNumber?.let {
            notificationPaginatedUseCase.getNotificationPaginated(userId = userId, pageNumber = usedPageNumber)
                ?.let { notificationListAndHasMoreNotificationsFlag ->
                    ResponseEntity.ok(notificationPaginatedJsonMapper.toJson(notificationListAndHasMoreNotificationsFlag))
                } ?: ResponseEntity.badRequest().body(Unit)
        } ?: ResponseEntity.badRequest().body(Unit)
    }
}