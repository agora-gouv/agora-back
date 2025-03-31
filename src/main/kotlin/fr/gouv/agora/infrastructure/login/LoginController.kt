package fr.gouv.agora.infrastructure.login

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.infrastructure.utils.IpAddressUtils
import fr.gouv.agora.security.jwt.AuthenticationTokenFilter.Companion.JWT_COOKIE_KEY
import fr.gouv.agora.security.jwt.JwtTokenUtils
import fr.gouv.agora.security.jwt.JwtTokenUtils.JWT_TOKEN_VALIDITY
import fr.gouv.agora.usecase.appVersionControl.AppVersionControlUseCase
import fr.gouv.agora.usecase.appVersionControl.AppVersionStatus.AUTHORIZED
import fr.gouv.agora.usecase.appVersionControl.AppVersionStatus.INVALID_APP
import fr.gouv.agora.usecase.appVersionControl.AppVersionStatus.UPDATE_REQUIRED
import fr.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.gouv.agora.usecase.login.LoginUseCase
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "Login")
class LoginController(
    private val appVersionControlUseCase: AppVersionControlUseCase,
    private val loginTokenGenerator: LoginTokenGenerator,
    private val loginUseCase: LoginUseCase,
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

        val appVersionStatus =
            appVersionControlUseCase.getAppVersionStatus(platform = platform, versionCode = versionCode)

        if (appVersionStatus == INVALID_APP) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
        }
        if (appVersionStatus == UPDATE_REQUIRED) {
            return ResponseEntity.status(HttpServletResponse.SC_PRECONDITION_FAILED).body(Unit)
        }
        if (appVersionStatus != AUTHORIZED) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
        }

        val loginTokenResult = loginTokenGenerator.decodeLoginToken(loginRequest.loginToken)
        if (loginTokenResult is DecodeResult.Failure) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
        }

        val userInfo = loginUseCase.login(
            loginRequest = LoginRequest(
                userId = (loginTokenResult as DecodeResult.Success).loginTokenData.userId,
                ipAddressHash = IpAddressUtils.retrieveIpAddressHash(request),
                userAgent = userAgent,
                fcmToken = fcmToken ?: "",
                platform = platform,
                versionName = versionName ?: "($versionCode)",
                versionCode = versionCode,
            )
        )

        if (userInfo == null) {
            return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED).body(Unit)
        }

        val (jwtToken, expirationEpochMilli) = JwtTokenUtils.generateToken(userId = userInfo.userId)
        val jwtCookie = ResponseCookie.from(JWT_COOKIE_KEY, jwtToken)
            .httpOnly(true)
            .secure(true)
            .path("/")
            .maxAge(JWT_TOKEN_VALIDITY)
            .build()

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .body(
                LoginInfoJson(
                    jwtToken = jwtToken,
                    jwtExpirationEpochMilli = expirationEpochMilli,
                    isModerator = false,
                )
            )
    }
}
