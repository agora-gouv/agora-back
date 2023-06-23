package fr.social.gouv.agora.usecase.qagArchive

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.qag.repository.QagArchiveResult
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qagUpdates.repository.QagUpdatesRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
class ArchiveOldQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val qagUpdatesRepository: QagUpdatesRepository,
    private val clock: Clock,
) {

    companion object {
        private const val DAYS_BEFORE_ARCHIVING_QAG = 14
    }

    fun archiveOldQag(): ArchiveQagListResult {
        val archivingStartTime = LocalDateTime.now(clock).minusDays(DAYS_BEFORE_ARCHIVING_QAG.toLong()).toDate()
        val qagOldList = qagInfoRepository.getAllQagInfo()
            .filter { qagInfo -> qagInfo.status == QagStatus.MODERATED_ACCEPTED || qagInfo.status == QagStatus.MODERATED_REJECTED }
            .filter { qagInfo ->
                val qagUpdates = qagUpdatesRepository.getQagUpdates(qagInfo.id)
                qagUpdates != null && qagUpdates.moderatedDate < archivingStartTime
            }
        return if (qagOldList.isNotEmpty()) {
            val qagArchiveListStatus = qagOldList.map { qagInfo -> qagInfoRepository.archiveQag(qagInfo.id) }
            if (QagArchiveResult.FAILURE in qagArchiveListStatus) ArchiveQagListResult.FAILURE
            else {
                qagInfoRepository.deleteQagListFromCache(qagOldList.map { qagInfo -> qagInfo.id })
                ArchiveQagListResult.SUCCESS
            }
        } else ArchiveQagListResult.FAILURE
    }
}

enum class ArchiveQagListResult { SUCCESS, FAILURE }