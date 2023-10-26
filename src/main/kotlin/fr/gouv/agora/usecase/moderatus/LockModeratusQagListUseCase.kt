package fr.gouv.agora.usecase.moderatus

import fr.gouv.agora.domain.ModeratusQagLockResult
import fr.gouv.agora.domain.QagLockResult
import fr.gouv.agora.domain.QagStatus
import fr.gouv.agora.usecase.moderatus.repository.ModeratusQagLockRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import org.springframework.stereotype.Service

@Service
class LockModeratusQagListUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val moderatusQagLockRepository: ModeratusQagLockRepository,
) {

    fun lockQagIds(lockedQagIds: List<String>): List<ModeratusQagLockResult> {
        val moderatusLockedQagIds = moderatusQagLockRepository.getLockedQagIds()

        return lockedQagIds.distinct().map { lockedQagId ->
            ModeratusQagLockResult(
                qagId = lockedQagId,
                lockResult = qagInfoRepository.getQagInfo(lockedQagId)
                    ?.takeIf { qagInfo -> qagInfo.status == QagStatus.OPEN }
                    ?.takeIf { _ -> !moderatusLockedQagIds.contains(lockedQagId) }
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