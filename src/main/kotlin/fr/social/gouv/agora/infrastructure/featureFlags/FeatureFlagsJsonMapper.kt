package fr.social.gouv.agora.infrastructure.featureFlags

import fr.social.gouv.agora.domain.FeatureFlags
import org.springframework.stereotype.Component

@Component
class FeatureFlagsJsonMapper {
     fun toJson(domain: FeatureFlags): FeatureFlagsJson {
         return FeatureFlagsJson(askQag = domain.isAskQuestionEnabled)
     }
}