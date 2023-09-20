package fr.social.gouv.agora.infrastructure.testNotification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.login.LoginUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class TestNotificationController(
    private val loginUseCase: LoginUseCase,
) {

    @GetMapping("/admin/testNotification")
    fun testNotification(
        @RequestHeader("Authorization") authorizationHeader: String,
        @RequestParam("userId") userId: String?,
        @RequestParam("title") title: String?,
        @RequestParam("description") description: String?,
        @RequestParam("type") type: String?,
        @RequestParam("qagId") qagId: String?,
        @RequestParam("consultationId") consultationId: String?,
        @RequestParam("step") step: String?,
    ): ResponseEntity<*> {
        val usedUserId =
            userId.takeUnless { it.isNullOrBlank() } ?: JwtTokenUtils.extractUserIdFromHeader(authorizationHeader)
        val usedTitle = title.takeUnless { it.isNullOrBlank() } ?: "Titre de la notification"
        val usedDescription = description.takeUnless { it.isNullOrBlank() } ?: "Description de la notification"

        return loginUseCase.findUser(usedUserId)?.let { userInfo ->
            val messageBuilder = Message.builder()
                .setNotification(
                    Notification.builder()
                        .setTitle(usedTitle)
                        .setBody(usedDescription)
                        .build()
                )
                .setToken(userInfo.fcmToken)
            type.takeUnless { it.isNullOrBlank() }.let { messageBuilder.putData("type", type) }
            qagId.takeUnless { it.isNullOrBlank() }.let { messageBuilder.putData("qagId", qagId) }
            step.takeUnless { it.isNullOrBlank() }.let { messageBuilder.putData("step", step) }
            consultationId.takeUnless { it.isNullOrBlank() }
                .let { messageBuilder.putData("consultationId", consultationId) }

            try {
                FirebaseMessaging.getInstance().send(messageBuilder.build())
                ResponseEntity.ok().body(Unit)
            } catch (e: FirebaseMessagingException) {
                ResponseEntity.badRequest().body(Unit)
            }
        } ?: ResponseEntity.badRequest().body(Unit)
    }

}