package fr.gouv.agora.infrastructure.notification

import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.usecase.notification.NotificationPaginatedUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class NotificationPaginatedController(
    private val notificationPaginatedUseCase: NotificationPaginatedUseCase,
    private val notificationPaginatedJsonMapper: NotificationPaginatedJsonMapper,
) {

    @GetMapping("/notifications/{pageNumber}")
    fun getNotificationList(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable pageNumber: String,
    ): ResponseEntity<*> {
        return getNotificationPaginatedJson(authorizationHeader, pageNumber)?.let { json ->
            ResponseEntity.ok(json)
        } ?: ResponseEntity.badRequest().body(Unit)
    }

    @GetMapping("/notifications/paginated/{pageNumber}")
    fun getNotificationPaginatedList(
        @RequestHeader("Authorization") authorizationHeader: String,
        @PathVariable pageNumber: String,
    ): ResponseEntity<*> {
        return getNotificationPaginatedJson(authorizationHeader, pageNumber)?.let { json ->
            ResponseEntity.ok(json)
        } ?: ResponseEntity.badRequest().body(Unit)
    }

    private fun getNotificationPaginatedJson(
        authorizationHeader: String,
        pageNumber: String,
    ): NotificationPaginatedJson? {
        val userId = JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        return pageNumber.toIntOrNull()?.let { pageNumberInt ->
            notificationPaginatedUseCase.getNotificationPaginated(userId = userId, pageNumber = pageNumberInt)
                ?.let { notificationListAndHasMoreNotificationsFlag ->
                    notificationPaginatedJsonMapper.toJson(notificationListAndHasMoreNotificationsFlag)
                }
        }
    }

}