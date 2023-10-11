package fr.social.gouv.agora.usecase.qagSelection

import fr.social.gouv.agora.domain.AgoraFeature
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.social.gouv.agora.usecase.qag.GetQagWithSupportAndThematiqueUseCase
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qag.repository.QagUpdateResult
import org.springframework.stereotype.Service

@Service
class SelectMostPopularQagUseCase(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val qagListUseCase: GetQagWithSupportAndThematiqueUseCase,
    private val filterGenerator: MostPopularQagFilterGenerator,
    private val qagInfoRepository: QagInfoRepository,
    private val randomQagSelector: RandomQagSelector,
) {

    fun putMostPopularQagInSelectedStatus() {
        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagSelect).not()) return

        println("🗳️ Selecting the most popular QaG...")
        qagListUseCase.getQagWithSupportAndThematique(qagFilters = filterGenerator.generateFilter())
            .takeIf { it.isNotEmpty() }
            ?.let { allQags ->
                val maxSupportCount = allQags.maxOf { qag -> qag.supportQagInfoList.size }
                val qagsWithMaxSupports = allQags.filter { qag -> qag.supportQagInfoList.size == maxSupportCount }
                val selectedQag = when (qagsWithMaxSupports.size) {
                    1 -> qagsWithMaxSupports.first()
                    else -> randomQagSelector.chooseRandom(qagsWithMaxSupports)
                }

                println("🗳️ Most popular QaG, with ${selectedQag.supportQagInfoList.size} supports is : ${selectedQag.qagInfo.title}")
                if (qagInfoRepository.selectQagForResponse(qagId = selectedQag.qagInfo.id) == QagUpdateResult.Failure) {
                    println("⚠️️ Select popular QaG error")
                }
            }
        println("🗳️ Selecting the most popular QaG finished !")
    }

}