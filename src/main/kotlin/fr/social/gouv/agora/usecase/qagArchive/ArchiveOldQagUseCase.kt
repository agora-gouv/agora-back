package fr.social.gouv.agora.usecase.qagArchive

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDate

@Service
class ArchiveOldQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val clock: Clock,
) {

    companion object {
        private const val DAYS_BEFORE_ARCHIVING_QAG = 14
    }

    fun archiveOldQag() {
        val qagOldList = qagInfoRepository.getAllQagInfo()
            .filter { qagInfo -> qagInfo.status == QagStatus.MODERATED_ACCEPTED || qagInfo.status == QagStatus.MODERATED_REJECTED }
            .filter { qagInfo ->
                qagInfo.date < LocalDate.now(clock).minusDays(DAYS_BEFORE_ARCHIVING_QAG.toLong()).toDate()
            }
        qagOldList.forEach { qagInfo -> qagInfoRepository.archiveQag(qagInfo.id) }
        qagInfoRepository.deleteQagList(qagOldList.map { qagInfo -> qagInfo.id })
    }
}