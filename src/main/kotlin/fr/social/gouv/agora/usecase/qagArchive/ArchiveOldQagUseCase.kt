package fr.social.gouv.agora.usecase.qagArchive

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.qag.GetQagWithSupportAndThematiqueUseCase
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ArchiveOldQagUseCase(
    private val qagListUseCase: GetQagWithSupportAndThematiqueUseCase,
    private val filterGenerator: MostPopularQagFilterGenerator,
    private val qagInfoRepository: QagInfoRepository,
) {

    companion object {
        private const val DAYS_BEFORE_ARCHIVING_QAG = 14
    }

    fun archiveOldQag() {
        val qagOldList = qagInfoRepository.getAllQagInfo()
            .filter { qagInfo -> qagInfo.status == QagStatus.MODERATED_ACCEPTED || qagInfo.status == QagStatus.MODERATED_REJECTED }
            .filter { qagInfo -> qagInfo.date < LocalDate.now().minusDays(DAYS_BEFORE_ARCHIVING_QAG.toLong()).toDate() }

        qagOldList.map { qagInfo ->  }
    }

    fun putMostPopularQagInSelectedStatus() {
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