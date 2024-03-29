package fr.gouv.agora.infrastructure.login

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.infrastructure.utils.IpAddressUtils
import fr.gouv.agora.usecase.appVersionControl.AppVersionControlUseCase
import fr.gouv.agora.usecase.appVersionControl.AppVersionStatus
import fr.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.gouv.agora.usecase.login.LoginUseCase
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController


@RestController
@Suppress("unused")
class SignupController(
    private val appVersionControlUseCase: AppVersionControlUseCase,
    private val loginUseCase: LoginUseCase,
    private val signupInfoJsonMapper: SignupInfoJsonMapper,
    private val featureFlagsUseCase: FeatureFlagsUseCase,
) {

    @PostMapping("/signup")
    fun signup(
        @RequestHeader("User-Agent") userAgent: String,
        @RequestHeader("fcmToken") fcmToken: String,
        @RequestHeader("versionName") versionName: String?,
        @RequestHeader("versionCode") versionCode: String,
        @RequestHeader("platform") platform: String,
        request: HttpServletRequest,
    ): ResponseEntity<*> {
        if (!featureFlagsUseCase.isFeatureEnabled(AgoraFeature.SignUp)) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
        }

        return when (appVersionControlUseCase.getAppVersionStatus(platform = platform, versionCode = versionCode)) {
            AppVersionStatus.INVALID_APP -> ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
            AppVersionStatus.UPDATE_REQUIRED -> ResponseEntity.status(HttpServletResponse.SC_PRECONDITION_FAILED)
                .body(Unit)

            AppVersionStatus.AUTHORIZED -> loginUseCase.signUp(
                SignupRequest(
                    ipAddressHash = IpAddressUtils.retrieveIpAddressHash(request),
                    userAgent = userAgent,
                    fcmToken = fcmToken,
                    platform = platform,
                    versionName = versionName ?: "($versionCode)",
                    versionCode = versionCode,
                )
            )?.let { userInfo ->
                signupInfoJsonMapper.toJson(domain = userInfo)?.let { userInfoJson ->
                    ResponseEntity.ok().body(userInfoJson)
                }
            } ?: ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
        }
    }
}