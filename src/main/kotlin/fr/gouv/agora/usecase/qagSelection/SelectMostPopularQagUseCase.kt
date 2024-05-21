package fr.gouv.agora.usecase.qagSelection

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qag.repository.QagUpdateResult
import fr.gouv.agora.usecase.responseQag.repository.ResponseQagPreviewCacheRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SelectMostPopularQagUseCase(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val randomQagSelector: RandomQagSelector,
    private val cacheRepository: ResponseQagPreviewCacheRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(SelectMostPopularQagUseCase::class.java)

    fun putMostPopularQagInSelectedStatus() {
        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagSelect).not()) return

        logger.info("🗳️ Selecting the most popular QaG...")
        val mostPopularQags = qagInfoRepository.getMostPopularQags()
        val selectedQag = when (mostPopularQags.size) {
            0 -> {
                logger.error("⚠️ Select popular QaG error: no QaGs to select")
                return
            }
            1 -> mostPopularQags.first()
            else -> randomQagSelector.chooseRandom(mostPopularQags)
        }

        logger.info("🗳️ Most popular QaG, with ${selectedQag.supportCount} supports is : ${selectedQag.title}")
        if (qagInfoRepository.selectQagForResponse(qagId = selectedQag.id) == QagUpdateResult.Failure) {
            logger.info("⚠️️ Select popular QaG error")
        }
        cacheRepository.evictResponseQagPreviewList()
        logger.info("🗳️ Selecting the most popular QaG finished !")
    }

}
