package fr.gouv.agora.usecase.qagArchive

import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
class ArchiveAllModeratedQagsUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val clock: Clock,
) {
    private val logger: Logger = LoggerFactory.getLogger(ArchiveAllModeratedQagsUseCase::class.java)

    data class ArchiveResult(
        val archivedCount: Int
    )

    fun archiveAllModeratedQags(): ArchiveResult {
        logger.info("📦 Archiving all moderated QaGs...")
        val resetDate = LocalDateTime.now(clock).withHour(14).withMinute(0).withSecond(0).toDate()
        val archivedCount = qagInfoRepository.archiveAllModeratedAcceptedQags(resetDate)

        logger.info("📦 Archived $archivedCount QaGs")
        return ArchiveResult(archivedCount)
    }
}
