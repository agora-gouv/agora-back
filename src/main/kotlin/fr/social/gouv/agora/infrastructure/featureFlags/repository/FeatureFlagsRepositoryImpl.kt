package fr.social.gouv.agora.infrastructure.featureFlags.repository

import fr.social.gouv.agora.domain.FeatureFlags
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class FeatureFlagsRepositoryImpl : FeatureFlagsRepository {

    override fun getFeatureFlags(): FeatureFlags {
        return FeatureFlags(
            isAskQuestionEnabled = System.getenv("IS_ASK_QUESTION_ENABLED").toBoolean(),
            isSignUpEnabled = System.getenv("IS_SIGNUP_ENABLED").toBoolean(),
            isLoginEnabled = System.getenv("IS_LOGIN_ENABLED").toBoolean(),
        )
    }

}