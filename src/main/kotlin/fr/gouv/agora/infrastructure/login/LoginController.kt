package fr.gouv.agora.infrastructure.login

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.usecase.appVersionControl.AppVersionControlUseCase
import fr.gouv.agora.usecase.appVersionControl.AppVersionStatus.*
import fr.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.gouv.agora.usecase.login.LoginUseCase
import jakarta.servlet.http.HttpServletRequest
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
    private val loginTokenGenerator: LoginTokenGenerator,
    private val loginUseCase: LoginUseCase,
    private val loginInfoJsonMapper: LoginInfoJsonMapper,
    private val featureFlagsUseCase: FeatureFlagsUseCase,
) {

    @PostMapping("/login")
    fun login(
        @RequestHeader("User-Agent") userAgent: String,
        @RequestHeader("fcmToken") fcmToken: String,
        @RequestHeader("versionName") versionName: String?,
        @RequestHeader("versionCode") versionCode: String,
        @RequestHeader("platform") platform: String,
        @RequestBody loginRequest: LoginRequestJson,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        if (!featureFlagsUseCase.isFeatureEnabled(AgoraFeature.Login)) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
        }

        return when (appVersionControlUseCase.getAppVersionStatus(platform = platform, versionCode = versionCode)) {
            INVALID_APP -> ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
            UPDATE_REQUIRED -> ResponseEntity.status(HttpServletResponse.SC_PRECONDITION_FAILED)
                .body(Unit)
            AUTHORIZED -> when (val loginTokenResult = loginTokenGenerator.decodeLoginToken(loginRequest.loginToken)) {
                DecodeResult.Failure -> ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
                is DecodeResult.Success -> loginUseCase.login(
                    loginRequest = LoginRequest(
                        userId = loginTokenResult.loginTokenData.userId,
                        remoteAddress = request.getHeader("X-Remote-Address") ?: request.remoteAddr,
                        userAgent = userAgent,
                        fcmToken = fcmToken,
                        platform = platform,
                        versionName = versionName ?: "($versionCode)",
                        versionCode = versionCode,
                    )
                )?.let { userInfo ->
                    ResponseEntity.ok().body(loginInfoJsonMapper.toJson(domain = userInfo))
                } ?: ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
            }
        }
    }

}