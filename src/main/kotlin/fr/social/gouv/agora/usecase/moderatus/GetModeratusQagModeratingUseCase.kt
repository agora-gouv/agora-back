package fr.social.gouv.agora.usecase.moderatus

import fr.social.gouv.agora.domain.ModeratusQag
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.infrastructure.utils.CollectionUtils.mapNotNullWhile
import fr.social.gouv.agora.usecase.moderatus.repository.ModeratusQagLockRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qagModerating.repository.QagModeratingLockRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import org.springframework.stereotype.Service

@Service
class GetModeratusQagListUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val qagModeratingLockRepository: QagModeratingLockRepository,
    private val moderatusQagLockRepository: ModeratusQagLockRepository,
    private val responseQagRepository: ResponseQagRepository,
    private val mapper: ModeratusQagMapper,
) {

    companion object {
        private const val MAX_MODERATING_LIST_SIZE = 100
    }

    fun getQagModeratingList(): List<ModeratusQag> {
        val moderatusLockedQagIds = moderatusQagLockRepository.getLockedQagIds()

        // TODO: rework
        return qagInfoRepository.getAllQagInfo()
            .asSequence()
            .filter { qagInfo -> qagInfo.status == QagStatus.OPEN }
            .filter { qagInfo -> !moderatusLockedQagIds.contains(qagInfo.id) }
            .filter { qagInfo -> qagModeratingLockRepository.getUserIdForQagLocked(qagInfo.id) == null }
            .filter { qagInfo -> responseQagRepository.getResponseQag(qagInfo.id) == null }
            .sortedBy { qagInfo -> qagInfo.date }
            .toList()
            .mapNotNullWhile(
                transformMethod = { qagInfo -> mapper.toModeratusQag(qagInfo) },
                loopWhileCondition = { qagModeratingList -> qagModeratingList.size < MAX_MODERATING_LIST_SIZE }
            )
    }

}