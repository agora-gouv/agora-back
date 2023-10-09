package fr.social.gouv.agora.usecase.qagArchive

import fr.social.gouv.agora.domain.AgoraFeature
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDateTime

@Service
class ArchiveOldQagUseCase(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val clock: Clock,
) {

    fun archiveOldQag(): ArchiveQagListResult {
        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.QagArchive).not()) return ArchiveQagListResult.FAILURE

        println("📜️ Archiving old QaGs...")
        val tuesdayThisWeek = LocalDateTime.now(clock).with(DayOfWeek.TUESDAY).withHour(14).withMinute(0).withSecond(0)
        qagInfoRepository.archiveOldQags(tuesdayThisWeek.toDate())
        println("📜️ Archiving old QaGs finished !")

        return ArchiveQagListResult.SUCCESS
    }
}

enum class ArchiveQagListResult { SUCCESS, FAILURE }