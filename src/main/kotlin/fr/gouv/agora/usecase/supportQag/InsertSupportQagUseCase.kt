package fr.gouv.agora.usecase.supportQag

import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.domain.SupportQagInserting
import fr.gouv.agora.usecase.qag.repository.QagDetailsCacheRepository
import fr.gouv.agora.usecase.qag.repository.QagInfo
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qagPaginated.repository.QagListsCacheRepository
import fr.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagCacheRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class InsertSupportQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val getSupportQagRepository: GetSupportQagRepository,
    private val supportQagRepository: SupportQagRepository,
    private val qagDetailsCacheRepository: QagDetailsCacheRepository,
    private val supportQagCacheRepository: SupportQagCacheRepository,
    private val qagListsCacheRepository: QagListsCacheRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(InsertSupportQagUseCase::class.java)

    fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult {
        if (getSupportQagRepository.isQagSupported(supportQagInserting.userId, supportQagInserting.qagId)) {
            logger.debug("⚠️ Add support error: already supported")
            return SupportQagResult.FAILURE
        }

        val qagInfo = qagInfoRepository.getQagInfo(supportQagInserting.qagId)
        return when (qagInfo?.status) {
            null, QagStatus.ARCHIVED, QagStatus.MODERATED_REJECTED, QagStatus.SELECTED_FOR_RESPONSE -> {
                logger.error("⚠️ Add support error: QaG with invalid status")
                SupportQagResult.FAILURE
            }

            QagStatus.OPEN, QagStatus.MODERATED_ACCEPTED -> {
                return supportQagRepository.insertSupportQag(supportQagInserting)
            }
        }
    }
}
