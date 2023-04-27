package fr.social.gouv.agora.infrastructure.notification

import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import com.google.firebase.messaging.Notification
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController


@RestController
class TestNotificationController {

    @GetMapping("/testNotification/{deviceFcmToken}")
    fun testNotification(@PathVariable deviceFcmToken: String): ResponseEntity<String> {
        val message = Message.builder()
            .setNotification(
                Notification.builder()
                    .setTitle("Nouvelle réponse QaG ! ^^")
                    .setBody("M. Stormtrooper nous fait l'honneur de répondre à la question: \"Quand l’application AGORA sera-t-elle disponible au grand public ?\"")
                    .build()
            )
            .putData("deeplink", "agora://qag.gouv.fr/889b41ad-321b-4338-8596-df745c546919")
            .setToken(deviceFcmToken)
            .build()

        val response = FirebaseMessaging.getInstance().send(message)
        return ResponseEntity.ok("FCM response = $response")
    }

}