package fr.social.gouv.agora.infrastructure.featureFlags.repository


import fr.social.gouv.agora.domain.FeatureFlags
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.stereotype.Component

@Component
class FeatureFlagsRepositoryImpl:FeatureFlagsRepository {

    override fun getFeatureFlags(): FeatureFlags {
        return FeatureFlags(isAskQuestionEnabled = System.getenv("IS_ASK_QUESTION_ENABLED").toBoolean())
    }


}




