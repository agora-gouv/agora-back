package fr.gouv.agora.infrastructure.login

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.infrastructure.utils.IpAddressUtils
import fr.gouv.agora.usecase.appVersionControl.AppVersionControlUseCase
import fr.gouv.agora.usecase.appVersionControl.AppVersionStatus.*
import fr.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.gouv.agora.usecase.login.LoginUseCase
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Suppress("unused")
@Tag(name = "Login")
class LoginController(
    private val appVersionControlUseCase: AppVersionControlUseCase,
    private val loginTokenGenerator: LoginTokenGenerator,
    private val loginUseCase: LoginUseCase,
    private val loginInfoJsonMapper: LoginInfoJsonMapper,
    private val featureFlagsUseCase: FeatureFlagsUseCase,
) {

    @PostMapping("/login")
    fun login(
        @RequestHeader("User-Agent") @Parameter(example = "Chrome/126.0.0.0") userAgent: String,
        @RequestHeader("fcmToken", required = false) fcmToken: String?,
        @RequestHeader("versionName", required = false) versionName: String?,
        @RequestHeader("versionCode") @Parameter(example = "1") versionCode: String,
        @RequestHeader("platform") @Parameter(example = "web") platform: String,
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
                        ipAddressHash = IpAddressUtils.retrieveIpAddressHash(request),
                        userAgent = userAgent,
                        fcmToken = fcmToken ?: "",
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
