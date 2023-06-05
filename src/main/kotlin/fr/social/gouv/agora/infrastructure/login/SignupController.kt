package fr.social.gouv.agora.infrastructure.login

import fr.social.gouv.agora.usecase.featureFlags.FeatureFlagsUseCase
import fr.social.gouv.agora.usecase.login.LoginUseCase
import org.springframework.http.HttpStatus
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
        val isSignUpEnabled = featureFlagsUseCase.getFeatureFlags().isSignUpEnabled
        return if (isSignUpEnabled)
            loginUseCase.signUp(fcmToken = fcmToken).let { userInfo ->
                signupInfoJsonMapper.toJson(domain = userInfo)?.let { userInfoJson ->
                    ResponseEntity.ok().body(userInfoJson)
                }
            } ?: ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Unit)
        else
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Unit)
    }
}