package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class QagWithSupportCountUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val supportQagRepository: GetSupportQagRepository,
    private val thematiqueRepository: ThematiqueRepository,
) {

    fun getDisplayedQagList(thematiqueId: String?): List<QagWithSupportCount> {
        val qagInfoList = qagInfoRepository.getDisplayedQagInfoList(thematiqueId = thematiqueId)
        val supportCounts = supportQagRepository.getQagSupportCounts(qagInfoList.map { it.id })
        val thematiqueList = thematiqueRepository.getThematiqueList()

        return qagInfoList.mapNotNull { qagInfo ->
            val supportCount = supportCounts[qagInfo.id]
            val thematique = thematiqueList.find { thematique -> thematique.id == qagInfo.thematiqueId }

            if (supportCount != null && thematique != null) {
                QagWithSupportCount(
                    qagInfo = qagInfo,
                    supportCount = supportCount,
                    thematique = thematique,
                )
            } else null
        }
    }

}

data class QagWithSupportCount(
    val qagInfo: QagInfo,
    val supportCount: Int,
    val thematique: Thematique,
)