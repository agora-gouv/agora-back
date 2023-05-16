package fr.social.gouv.agora.infrastructure.login

import fr.social.gouv.agora.security.cipher.DecodeResult
import fr.social.gouv.agora.security.cipher.LoginTokenGenerator
import fr.social.gouv.agora.usecase.login.LoginUseCase
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class LoginController(private val loginUseCase: LoginUseCase, private val loginInfoJsonMapper: LoginInfoJsonMapper) {

    @PostMapping("/login")
    fun login(
        @RequestHeader("deviceId") deviceId: String,
        @RequestHeader("fcmToken") fcmToken: String,
        @RequestBody loginToken: String,
    ): ResponseEntity<*> {
        return when (val loginTokenResult = LoginTokenGenerator.decodeLoginToken(loginToken)) {
            DecodeResult.Failure -> ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
            is DecodeResult.Success -> loginUseCase.login(
                deviceId = deviceId,
                loginTokenData = loginTokenResult.loginTokenData,
                fcmToken = fcmToken,
            )?.let { userInfo ->
                ResponseEntity.ok().body(loginInfoJsonMapper.toJson(domain = userInfo))
            } ?: ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
        }
    }

}