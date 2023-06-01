package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagPreview
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.utils.CollectionUtils.mapNotNullWhile
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetQagLatestPreviewListUseCase(
    private val thematiqueRepository: ThematiqueRepository,
    private val supportRepository: GetSupportQagRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val responseQagRepository: ResponseQagRepository,
    private val mapper: QagPreviewMapper,
) {
    companion object {
        private const val MAX_PREVIEW_LIST_SIZE = 10
    }

    fun getQagLatestPreviewList(userId: String, thematiqueId: String?): List<QagPreview> {
        val thematiqueMap = mutableMapOf<String, Thematique?>()

        return qagInfoRepository.getAllQagInfo()
            .filter { qagInfo -> thematiqueId.isNullOrBlank() || qagInfo.thematiqueId == thematiqueId }
            .sortedByDescending { it.date }
            .mapNotNullWhile(
                transformMethod = { qagInfo -> toQagPreview(qagInfo, userId, thematiqueMap) },
                loopWhileCondition = { qagPreviewList -> qagPreviewList.size < MAX_PREVIEW_LIST_SIZE },
            )
    }

    private fun toQagPreview(
        qagInfo: QagInfo,
        userId: String,
        thematiqueMap: MutableMap<String, Thematique?>
    ): QagPreview? {
        if (responseQagRepository.getResponseQag(qagId = qagInfo.id) != null) return null
        return getThematique(qagInfo.thematiqueId, thematiqueMap)?.let { thematique ->
            supportRepository.getSupportQag(qagId = qagInfo.id, userId = userId)?.let { supportQag ->
                mapper.toPreview(qagInfo = qagInfo, thematique = thematique, supportQag = supportQag)
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
