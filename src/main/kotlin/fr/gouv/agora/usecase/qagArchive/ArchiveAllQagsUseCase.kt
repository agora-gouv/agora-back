package fr.gouv.agora.usecase.qagArchive

import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ArchiveAllQagsUseCase(
    private val qagInfoRepository: QagInfoRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(ArchiveAllQagsUseCase::class.java)

    data class ArchiveResult(
        val archivedCount: Int
    )

    fun execute(): ArchiveResult {
        logger.info("📦 Archiving all moderated QaGs...")
        val archivedCount = qagInfoRepository.archiveAllAcceptedQags()

        logger.info("📦 Archived $archivedCount QaGs")
        return ArchiveResult(archivedCount)
    }
}
