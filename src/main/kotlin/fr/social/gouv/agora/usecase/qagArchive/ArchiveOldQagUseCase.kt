package fr.social.gouv.agora.usecase.qagArchive

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.social.gouv.agora.usecase.qag.repository.QagArchiveResult
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
class ArchiveOldQagUseCase(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val qagUpdatesRepository: QagUpdatesRepository,
    private val clock: Clock,
) {

    companion object {
        private const val DAYS_BEFORE_ARCHIVING_QAG = 14
    }

    fun archiveOldQag(): ArchiveQagListResult {
        if (featureFlagsRepository.getFeatureFlags().isQagArchiveEnabled.not()) return ArchiveQagListResult.FAILURE

        val archivingStartTime = LocalDateTime.now(clock).minusDays(DAYS_BEFORE_ARCHIVING_QAG.toLong()).toDate()
        val qagModeratedIdList = qagInfoRepository.getAllQagInfo()
            .filter { qagInfo -> qagInfo.status == QagStatus.MODERATED_ACCEPTED || qagInfo.status == QagStatus.MODERATED_REJECTED }
            .map { qagInfo -> qagInfo.id }
        return if (qagModeratedIdList.isEmpty())
            ArchiveQagListResult.FAILURE
        else {
            val qagToArchiveList = qagUpdatesRepository.getQagUpdates(qagModeratedIdList)
                .filter { qagUpdates -> qagUpdates.moderatedDate < archivingStartTime }
            if (qagToArchiveList.isNotEmpty()) {
                val qagArchiveListStatus =
                    qagToArchiveList.map { qagUpdates -> qagInfoRepository.archiveQag(qagUpdates.qagId) }
                if (QagArchiveResult.FAILURE in qagArchiveListStatus) ArchiveQagListResult.FAILURE
                else {
                    qagInfoRepository.deleteQagListFromCache(qagToArchiveList.map { qagUpdates -> qagUpdates.qagId })
                    ArchiveQagListResult.SUCCESS
                }
            } else ArchiveQagListResult.FAILURE
        }
    }
}

enum class ArchiveQagListResult { SUCCESS, FAILURE }