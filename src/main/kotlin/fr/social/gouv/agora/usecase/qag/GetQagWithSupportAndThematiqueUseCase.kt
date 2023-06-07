package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.SupportQagInfo
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.responseQag.repository.ResponseQagRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetQagWithSupportAndThematiqueUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val responseQagRepository: ResponseQagRepository,
    private val supportQagRepository: GetSupportQagRepository,
    private val thematiqueRepository: ThematiqueRepository,
) {

    fun getQagWithSupportAndThematique(qagFilters: QagFilters): List<QagInfoWithSupportAndThematique> {
        return qagInfoRepository.getAllQagInfo()
            .filter { qagInfo -> qagFilters.filterQagInfo.invoke(qagInfo) }
            .takeUnless { allQagInfo -> allQagInfo.isEmpty() }
            ?.removeQagWithResponses()
            ?.retrieveSupportQag(qagFilters)
            ?.retrieveThematique()
            ?: emptyList()
    }

    private fun List<QagInfo>.removeQagWithResponses(): List<QagInfo> {
        val allResponseQag = responseQagRepository.getAllResponseQag()
        return this.filterNot { qagInfo -> allResponseQag.any { responseQag -> responseQag.qagId == qagInfo.id } }
    }

    private fun List<QagInfo>.retrieveSupportQag(qagFilters: QagFilters): List<QagInfoWithSupport> {
        val allSupportQagInfo = supportQagRepository.getAllSupportQag()
        return this.mapNotNull { qagInfo ->
            QagInfoWithSupport(
                qagInfo = qagInfo,
                supportQagList = allSupportQagInfo.filter { supportQagInfo ->
                    supportQagInfo.qagId == qagInfo.id && qagFilters.filterSupportQagInfo.invoke(supportQagInfo)
                },
            ).takeIf { qagFilters.filterSupportQagInfoList.invoke(it.supportQagList) }
        }
    }

    private fun List<QagInfoWithSupport>.retrieveThematique(): List<QagInfoWithSupportAndThematique> {
        val allThematique = thematiqueRepository.getThematiqueList()
        return this.mapNotNull { qagInfoWithSupport ->
            allThematique.find { thematique -> thematique.id == qagInfoWithSupport.qagInfo.thematiqueId }
                ?.let { thematique ->
                    QagInfoWithSupportAndThematique(
                        qagInfo = qagInfoWithSupport.qagInfo,
                        supportQagInfoList = qagInfoWithSupport.supportQagList,
                        thematique = thematique,
                    )
                }
        }
    }

}

data class QagFilters(
    val filterQagInfo: (QagInfo) -> Boolean = { _ -> true },
    val filterSupportQagInfo: (SupportQagInfo) -> Boolean = { _ -> true },
    val filterSupportQagInfoList: (List<SupportQagInfo>) -> Boolean = { _ -> true },
)

private data class QagInfoWithSupport(
    val qagInfo: QagInfo,
    val supportQagList: List<SupportQagInfo>,
)

data class QagInfoWithSupportAndThematique(
    val qagInfo: QagInfo,
    val supportQagInfoList: List<SupportQagInfo>,
    val thematique: Thematique,
)