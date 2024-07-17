package fr.gouv.agora.infrastructure.login

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.infrastructure.utils.IpAddressUtils
import fr.gouv.agora.usecase.appVersionControl.AppVersionControlUseCase
import fr.gouv.agora.usecase.appVersionControl.AppVersionStatus
import fr.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.gouv.agora.usecase.login.LoginUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.parameters.RequestBody
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController


@RestController
@Suppress("unused")
@Tag(
    name = "Authentification (Sign up)",
    description = "Controller pour s'authentifier et récupérer un token d'authentification"
)
class SignupController(
    private val appVersionControlUseCase: AppVersionControlUseCase,
    private val loginUseCase: LoginUseCase,
    private val signupInfoJsonMapper: SignupInfoJsonMapper,
    private val featureFlagsUseCase: FeatureFlagsUseCase,
) {
    @PostMapping("/signup")
    @Operation(
        summary = "S'Authentifier pour obtenir un token",
        description = "Retourne un code 200 si tout s'est bien passé"
    )
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
