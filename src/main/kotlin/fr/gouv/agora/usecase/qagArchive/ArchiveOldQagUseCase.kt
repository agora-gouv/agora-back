package fr.gouv.agora.usecase.qagArchive

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.qag.repository.AskQagStatusCacheRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.qagPaginated.repository.QagListsCacheRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime

@Service
class ArchiveOldQagUseCase(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val qagListsCacheRepository: QagListsCacheRepository,
    private val clock: Clock,
) {
    private val logger: Logger = LoggerFactory.getLogger(ArchiveOldQagUseCase::class.java)

    fun archiveOldQag(): ArchiveQagListResult {
        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagSelect).not()) return ArchiveQagListResult.FAILURE

        logger.info("📜️ Archiving old QaGs...")
        val mondayThisWeek = LocalDateTime.now(clock).with(DayOfWeek.MONDAY).withHour(14).withMinute(0).withSecond(0)
        qagInfoRepository.archiveOldQags(mondayThisWeek.toDate())
        logger.info("📜️ Archiving old QaGs finished !")

        return ArchiveQagListResult.SUCCESS
    }
}

enum class ArchiveQagListResult { SUCCESS, FAILURE }
