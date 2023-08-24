package fr.social.gouv.agora.usecase.qagModerating

import fr.social.gouv.agora.domain.QagModerating
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.utils.CollectionUtils.mapNotNullWhile
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qagModerating.repository.QagModeratingLockRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetQagModeratingListUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val responseQagRepository: ResponseQagRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val supportRepository: GetSupportQagRepository,
    private val qagModeratingLockRepository: QagModeratingLockRepository,
    private val mapper: QagModeratingMapper,
) {

    companion object {
        private const val MAX_MODERATING_LIST_SIZE = 20
    }

    fun getQagModeratingList(userId: String): List<QagModerating> {
        val thematiqueMap = mutableMapOf<String, Thematique?>()

        // TODO 260: do not return locked QaG for Moderatus
        return qagInfoRepository.getAllQagInfo()
            .filter { qagInfo -> qagInfo.status == QagStatus.OPEN }
            .filter { qagInfo ->
                val lockingUserId = qagModeratingLockRepository.getUserIdForQagLocked(qagInfo.id)
                lockingUserId == null || lockingUserId == userId
            }
            .sortedBy { qagInfo -> qagInfo.date }
            .mapNotNullWhile(
                transformMethod = { qagInfo -> toQagModerating(qagInfo, userId, thematiqueMap) },
                loopWhileCondition = { qagModeratingList -> qagModeratingList.size < MAX_MODERATING_LIST_SIZE },
            ).onEach { qagModerating ->
                qagModeratingLockRepository.setQagLocked(
                    qagId = qagModerating.id,
                    userId = userId,
                )
            }
    }

    fun getModeratingQagCount(): Int {
        val allResponseQag = responseQagRepository.getAllResponseQag()
        return qagInfoRepository.getAllQagInfo()
            .filter { qagInfo -> qagInfo.status == QagStatus.OPEN }
            .filterNot { qagInfo -> allResponseQag.any { responseQag -> responseQag.qagId == qagInfo.id } }
            .count()
    }

    private fun toQagModerating(
        qagInfo: QagInfo,
        userId: String,
        thematiqueMap: MutableMap<String, Thematique?>,
    ): QagModerating? {
        if (responseQagRepository.getResponseQag(qagId = qagInfo.id) != null) return null
        return getThematique(qagInfo.thematiqueId, thematiqueMap)?.let { thematique ->
            supportRepository.getSupportQag(qagId = qagInfo.id, userId = userId)?.let { supportQag ->
                mapper.toQagModerating(qagInfo = qagInfo, thematique = thematique, supportQag = supportQag)
            }
        }
    }

    private fun getThematique(
        thematiqueId: String,
        thematiqueMap: MutableMap<String, Thematique?>,
    ): Thematique? {
        return if (thematiqueMap.containsKey(thematiqueId)) {
            thematiqueMap[thematiqueId]
        } else {
            thematiqueRepository.getThematique(thematiqueId).also { thematique ->
                thematiqueMap[thematiqueId] = thematique
            }
        }
    }

}
