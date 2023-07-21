package fr.social.gouv.agora.infrastructure.login

import fr.social.gouv.agora.usecase.appVersionControl.AppVersionControlUseCase
import fr.social.gouv.agora.usecase.appVersionControl.AppVersionStatus
import fr.social.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.social.gouv.agora.usecase.login.LoginUseCase
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.*


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
        @RequestHeader("fcmToken") fcmToken: String,
        @RequestHeader("versionName") versionName: String?,
        @RequestHeader("versionCode") versionCode: String,
        @RequestHeader("platform") platform: String,
    ): ResponseEntity<*> {
        if (!featureFlagsUseCase.getFeatureFlags().isSignUpEnabled) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
        }

        return when (appVersionControlUseCase.getAppVersionStatus(platform = platform, versionCode = versionCode)) {
            AppVersionStatus.INVALID_APP -> ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
            AppVersionStatus.UPDATE_REQUIRED -> ResponseEntity.status(HttpServletResponse.SC_PRECONDITION_FAILED)
                .body(Unit)
            AppVersionStatus.AUTHORIZED -> loginUseCase.signUp(fcmToken = fcmToken).let { userInfo ->
                signupInfoJsonMapper.toJson(domain = userInfo)?.let { userInfoJson ->
                    ResponseEntity.ok().body(userInfoJson)
                }
            } ?: ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
        }
    }
}