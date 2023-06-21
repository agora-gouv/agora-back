package fr.social.gouv.agora.usecase.qagArchive

import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ArchiveOldQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
) {

    companion object {
        private const val DAYS_BEFORE_ARCHIVING_QAG = 14
    }

    fun archiveOldQag() {
        val qagOldList = qagInfoRepository.getAllQagInfo()
            .filter { qagInfo -> qagInfo.status == QagStatus.MODERATED_ACCEPTED || qagInfo.status == QagStatus.MODERATED_REJECTED }
            .filter { qagInfo -> qagInfo.date < LocalDate.now().minusDays(DAYS_BEFORE_ARCHIVING_QAG.toLong()).toDate() }
        qagOldList.map { qagInfo -> qagInfoRepository.archiveQag(qagInfo.id) }
    }
}