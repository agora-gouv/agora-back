package fr.social.gouv.agora.usecase.qagSelection

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.social.gouv.agora.usecase.qag.GetQagWithSupportAndThematiqueUseCase
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
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
        if (featureFlagsRepository.getFeatureFlags().isQagSelectEnabled.not()) return

        qagListUseCase.getQagWithSupportAndThematique(qagFilters = filterGenerator.generateFilter())
            .takeIf { it.isNotEmpty() }
            ?.let { allQags ->
                val maxSupportCount = allQags.maxOf { qag -> qag.supportQagInfoList.size }
                val qagsWithMaxSupports = allQags.filter { qag -> qag.supportQagInfoList.size == maxSupportCount }

                val selectedQag = when (qagsWithMaxSupports.size) {
                    1 -> qagsWithMaxSupports.first()
                    else -> randomQagSelector.chooseRandom(qagsWithMaxSupports)
                }

                qagInfoRepository.updateQagStatus(
                    qagId = selectedQag.qagInfo.id,
                    newQagStatus = QagStatus.SELECTED_FOR_RESPONSE,
                )
            }
    }

}