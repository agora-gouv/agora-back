package fr.social.gouv.agora.infrastructure.login

import fr.social.gouv.agora.usecase.login.LoginUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class LoginController(private val loginUseCase: LoginUseCase, private val userInfoJsonMapper: UserInfoJsonMapper) {

    @PostMapping("/login")
    fun login(
        @RequestHeader("deviceId") deviceId: String,
        @RequestHeader("fcmToken") fcmToken: String,
    ): ResponseEntity<*> {
        val userInfo = loginUseCase.login(
            deviceId = deviceId,
            fcmToken = fcmToken,
        )
        return userInfo?.let {
            ResponseEntity.ok(userInfoJsonMapper.toJson(userInfo))
        } ?: ResponseEntity.status(400).body(Unit)
    }

}