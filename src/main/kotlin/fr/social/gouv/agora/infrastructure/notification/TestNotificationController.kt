package fr.social.gouv.agora.infrastructure.notification

import fr.social.gouv.agora.infrastructure.login.repository.LoginDatabaseRepository
import fr.social.gouv.agora.infrastructure.supportQag.repository.SupportQagDatabaseRepository
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@Suppress("unused")
class TestNotificationController(
    private val supportQagDatabaseRepository: SupportQagDatabaseRepository,
    private val loginDatabaseRepository: LoginDatabaseRepository,
) {

//    @GetMapping("admin/testNotification")
//    fun testNotification(): ResponseEntity<String> {
//
//        val retraiteQagId = UUID.fromString("f29c5d6f-9838-4c57-a7ec-0612145bb0c8")
//        val supportQagDTOs = supportQagDatabaseRepository.getSupportQagList(retraiteQagId)
//
//        supportQagDTOs.mapNotNull { supportQagDTO ->
//            loginDatabaseRepository.getUserByDeviceId(supportQagDTO.userId)?.fcmToken.takeUnless { it.isNullOrEmpty() }
//        }.takeUnless { it.isEmpty() }?.let { fcmTokens ->
//            val message = MulticastMessage.builder()
//                .setNotification(
//                    Notification.builder()
//                        .setTitle("Merci pour votre soutien ! ^^")
//                        .setBody("Merci de soutenir la question d'Harry P. à propos de la retraite !")
//                        .build()
//                )
//                .addAllTokens(fcmTokens)
//                .build()
//
//            val response = FirebaseMessaging.getInstance().sendMulticast(message)
//            return ResponseEntity.ok("Sent message to ${response.successCount} successfully!")
//        }
//
//        return ResponseEntity.status(400).body("No fcmToken found")
//    }

}