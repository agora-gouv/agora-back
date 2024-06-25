package fr.gouv.agora.usecase.suspiciousUser

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class DeleteSuspiciousSupportsUseCase(
    val supportQagRepository: SupportQagRepository,
    val featureFlagsRepository: FeatureFlagsRepository,
) {
    private val logger = LoggerFactory.getLogger(this::class.java)

    fun execute() {
        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.DeleteBannedUsersSupports)) return
        logger.info("🏴 Remove supports of banned users")
        val numberOfRemovedSupports = supportQagRepository.deleteBannedUsersLastWeekSupportsOnUnselectedQags()
        logger.info("🏴 $numberOfRemovedSupports banned users' supports has been removed")
    }
}
