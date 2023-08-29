package fr.social.gouv.agora.usecase.moderatus

import fr.social.gouv.agora.domain.ModeratusQagLockResult
import fr.social.gouv.agora.domain.QagLockResult
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.moderatus.repository.ModeratusQagLockRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qagModerating.repository.QagModeratingLockRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import org.springframework.stereotype.Service

@Service
class LockModeratusQagListUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val qagModeratingLockRepository: QagModeratingLockRepository,
    private val moderatusQagLockRepository: ModeratusQagLockRepository,
    private val responseQagRepository: ResponseQagRepository,
) {

    fun lockQagIds(lockedQagIds: List<String>): List<ModeratusQagLockResult> {
        val moderatusLockedQagIds = moderatusQagLockRepository.getLockedQagIds()

        return lockedQagIds.distinct().map { lockedQagId ->
            ModeratusQagLockResult(
                qagId = lockedQagId,
                lockResult = qagInfoRepository.getQagInfo(lockedQagId)
                    ?.takeIf { qagInfo -> qagInfo.status == QagStatus.OPEN }
                    ?.takeIf { _ -> !moderatusLockedQagIds.contains(lockedQagId) }
                    ?.takeIf { _ -> qagModeratingLockRepository.getUserIdForQagLocked(lockedQagId) == null }
                    ?.takeIf { _ -> responseQagRepository.getResponseQag(lockedQagId) == null }
                    ?.let { QagLockResult.SUCCESS } ?: QagLockResult.NOT_FOUND
            )
        }.also { lockedQagResults ->
            val additionalLockedQagIds = lockedQagResults
                .filter { lockedQagResult -> lockedQagResult.lockResult == QagLockResult.SUCCESS }
                .map { lockedQagResult -> lockedQagResult.qagId }

            if (additionalLockedQagIds.isNotEmpty()) {
                moderatusQagLockRepository.addLockedIds(additionalLockedQagIds)
            }
        }

    }

}