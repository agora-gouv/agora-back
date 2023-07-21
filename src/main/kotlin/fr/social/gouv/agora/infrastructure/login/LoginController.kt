package fr.social.gouv.agora.infrastructure.login

import fr.social.gouv.agora.usecase.appVersionControl.AppVersionControlUseCase
import fr.social.gouv.agora.usecase.appVersionControl.AppVersionStatus.*
import fr.social.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.social.gouv.agora.usecase.login.LoginUseCase
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
class LoginController(
    private val appVersionControlUseCase: AppVersionControlUseCase,
    private val loginUseCase: LoginUseCase,
    private val loginInfoJsonMapper: LoginInfoJsonMapper,
    private val featureFlagsUseCase: FeatureFlagsUseCase,
) {

    @PostMapping("/login")
    fun login(
        @RequestHeader("fcmToken") fcmToken: String,
        @RequestHeader("versionName") versionName: String?,
        @RequestHeader("versionCode") versionCode: String,
        @RequestHeader("platform") platform: String,
        @RequestBody loginToken: String,
    ): ResponseEntity<*> {
        if (!featureFlagsUseCase.getFeatureFlags().isLoginEnabled) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
        }

        return when (appVersionControlUseCase.getAppVersionStatus(platform = platform, versionCode = versionCode)) {
            INVALID_APP -> ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
            UPDATE_REQUIRED -> ResponseEntity.status(HttpServletResponse.SC_PRECONDITION_FAILED)
                .body(Unit)
            AUTHORIZED -> when (val loginTokenResult = LoginTokenGenerator.decodeLoginToken(loginToken)) {
                DecodeResult.Failure -> ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
                is DecodeResult.Success -> loginUseCase.login(
                    loginTokenData = loginTokenResult.loginTokenData,
                    fcmToken = fcmToken,
                )?.let { userInfo ->
                    ResponseEntity.ok().body(loginInfoJsonMapper.toJson(domain = userInfo))
                } ?: ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
            }
        }
    }

}