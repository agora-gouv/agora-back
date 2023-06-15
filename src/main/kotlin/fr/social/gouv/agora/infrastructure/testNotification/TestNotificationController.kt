package fr.social.gouv.agora.infrastructure.testNotification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingException
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import fr.social.gouv.agora.security.jwt.JwtTokenUtils
import fr.social.gouv.agora.usecase.login.LoginUseCase
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class TestNotificationController(
    private val loginUseCase: LoginUseCase,
) {

    @GetMapping("/moderate/testNotification")
    fun testNotification(@RequestHeader("Authorization") authorizationHeader: String): ResponseEntity<*> {
        return loginUseCase.findUser(JwtTokenUtils.extractUserIdFromHeader(authorizationHeader))?.let { userInfo ->
            val message = Message.builder()
                .setNotification(
                    Notification.builder()
                        .setTitle("Titre de la notification")
                        .setBody("Description de la notification")
                        .build()
                )
                .setToken(userInfo.fcmToken)
                .putData("type", "qagDetails")
                .putData("qagId", "996436ca-ee69-11ed-a05b-0242ac120003")
                .build()

            try {
                FirebaseMessaging.getInstance().send(message)
                ResponseEntity.ok().body(Unit)
            } catch (e: FirebaseMessagingException) {
                ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Unit)
            }
        } ?: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Unit)
    }

}