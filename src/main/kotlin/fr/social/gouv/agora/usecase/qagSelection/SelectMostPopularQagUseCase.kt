package fr.social.gouv.agora.usecase.qagSelection

import fr.social.gouv.agora.domain.AgoraFeature
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagUpdateResult
import org.springframework.stereotype.Service

@Service
class SelectMostPopularQagUseCase(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val randomQagSelector: RandomQagSelector,
) {

    fun putMostPopularQagInSelectedStatus() {
        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagSelect).not()) return

        println("🗳️ Selecting the most popular QaG...")
        val mostPopularQags = qagInfoRepository.getMostPopularQags()
        val selectedQag = when (mostPopularQags.size) {
            0 -> {
                println("⚠️ Select popular QaG error: no QaGs to select")
                return
            }
            1 -> mostPopularQags.first()
            else -> randomQagSelector.chooseRandom(mostPopularQags)
        }

        println("🗳️ Most popular QaG, with ${selectedQag.supportCount} supports is : ${selectedQag.title}")
        if (qagInfoRepository.selectQagForResponse(qagId = selectedQag.id) == QagUpdateResult.Failure) {
            println("⚠️️ Select popular QaG error")
        }
        println("🗳️ Selecting the most popular QaG finished !")
    }

}