package fr.social.gouv.agora.infrastructure.login

import fr.social.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.social.gouv.agora.usecase.login.LoginUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RestController
import java.util.*


@RestController
@Suppress("unused")
class SignupController(
    private val loginUseCase: LoginUseCase,
    private val signupInfoJsonMapper: SignupInfoJsonMapper,
    private val featureFlagsUseCase: FeatureFlagsUseCase,
) {

    @PostMapping("/signup")
    fun signup(
        @RequestHeader("fcmToken") fcmToken: String,
    ): ResponseEntity<*> {
        var isSignUpEnabled = featureFlagsUseCase.getFeatureFlags().isSignUpEnabled
        isSignUpEnabled = true //TODO suppress when featureFlags are configured in system
        return if (isSignUpEnabled)
            loginUseCase.signUp(
                fcmToken = fcmToken,
            )?.let { userInfo ->
                signupInfoJsonMapper.toJson(domain = userInfo)?.let { userInfoJson ->
                    ResponseEntity.ok().body(userInfoJson)
                }
            } ?: ResponseEntity.status(400).body(Unit)
        else
            ResponseEntity.status(400).body(Unit)
    }
}