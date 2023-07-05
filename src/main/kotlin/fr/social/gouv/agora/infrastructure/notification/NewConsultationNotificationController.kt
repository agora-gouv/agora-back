package fr.social.gouv.agora.infrastructure.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.usecase.login.LoginUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@Suppress("unused")
class NewConsultationNotificationController(
    private val loginUseCase: LoginUseCase,
) {

    @GetMapping("/admin/consultations/{consultationId}")
    fun newConsultationNotification(
        @RequestParam("title") title: String,
        @RequestParam("description") description: String,
        @RequestParam("type") type: String,
        @PathVariable("consultationId") consultationId: String,
    ): ResponseEntity<*> {
        val usedTitle = title.takeUnless { it.isBlank() } ?: "Titre de la notification"
        val usedDescription = description.takeUnless { it.isBlank() } ?: "Description de la notification"
        val usedType = type.takeUnless { it.isBlank() }
        val usersList = usedType?.let { getUsersList(usedType) }

        usersList?.map { userInfo ->
            val messageBuilder = Message.builder()
                .setNotification(
                    Notification.builder()
                        .setTitle(usedTitle)
                        .setBody(usedDescription)
                        .build()
                ).setToken(userInfo.fcmToken)
            usedType.let { messageBuilder.putData("type", usedType) }
            consultationId.takeUnless { it.isBlank() }
                .let { messageBuilder.putData("consultationId", consultationId) }
            try {
                FirebaseMessaging.getInstance().send(messageBuilder.build())
            } catch (e: FirebaseMessagingException) {
                return ResponseEntity.badRequest().body(Unit)
            }
        }

        return ResponseEntity.ok().body(Unit)
    }

    private fun getUsersList(type: String): List<UserInfo> {
        return emptyList()
    }

}