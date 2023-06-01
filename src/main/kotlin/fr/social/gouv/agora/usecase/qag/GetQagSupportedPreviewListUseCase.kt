package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.SupportQagInfo
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.utils.CollectionUtils.mapNotNullWhile
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetQagSupportedPreviewListUseCase(
    private val thematiqueRepository: ThematiqueRepository,
    private val supportRepository: GetSupportQagRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val responseQagRepository: ResponseQagRepository,
    private val mapper: QagPreviewMapper,
) {

    companion object {
        private const val MAX_PREVIEW_LIST_SIZE = 10
    }

    fun getQagSupportedPreviewList(userId: String, thematiqueId: String?): List<QagPreview> {
        val thematiqueMap = mutableMapOf<String, Thematique?>()

        val allQagInfo = qagInfoRepository.getAllQagInfo()
        val allSupportQag = supportRepository.getAllSupportQag()
        val allQagAndSupport = allQagInfo.mapNotNull { qagInfo ->
            val supportQagInfoList = allSupportQag.filter { supportQagInfo -> supportQagInfo.qagId == qagInfo.id }
            supportQagInfoList.find { supportQagInfo -> supportQagInfo.userId == userId }
                ?.let { userSupportQagInfo -> QagInfoAndSupport(qagInfo, userSupportQagInfo, supportQagInfoList) }
        }

        return allQagAndSupport
            .filter { qagInfoAndSupport -> thematiqueId == null || qagInfoAndSupport.qagInfo.thematiqueId == thematiqueId }
            .sortedByDescending { qagInfoAndSupport -> qagInfoAndSupport.userSupportQagInfo.supportDate }
            .mapNotNullWhile(
                transformMethod = { qagInfoAndSupport ->
                    toQagPreview(
                        qagInfo = qagInfoAndSupport.qagInfo,
                        supportQagList = qagInfoAndSupport.supportQagInfoList,
                        userId = userId,
                        thematiqueMap = thematiqueMap
                    )
                },
                loopWhileCondition = { qagPreviewList -> qagPreviewList.size < MAX_PREVIEW_LIST_SIZE }
            )
    }

    private fun toQagPreview(
        qagInfo: QagInfo,
        supportQagList: List<SupportQagInfo>,
        userId: String,
        thematiqueMap: MutableMap<String, Thematique?>
    ): QagPreview? {
        return if (responseQagRepository.getResponseQag(qagId = qagInfo.id) != null) null
        else {
            getThematique(qagInfo.thematiqueId, thematiqueMap)?.let { thematique ->
                mapper.toPreview(
                    qagInfo = qagInfo,
                    thematique = thematique,
                    supportQagInfoList = supportQagList,
                    userId = userId,
                )
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

    private data class QagInfoAndSupport(
        val qagInfo: QagInfo,
        val userSupportQagInfo: SupportQagInfo,
        val supportQagInfoList: List<SupportQagInfo>,
    )
}
