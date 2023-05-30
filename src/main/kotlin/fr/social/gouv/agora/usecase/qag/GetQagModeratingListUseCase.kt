package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagModerating
import fr.social.gouv.agora.usecase.qag.repository.QagModeratingListRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetQagModeratingListUseCase(
    private val thematiqueRepository: ThematiqueRepository,
    private val supportRepository: GetSupportQagRepository,
    private val qagModeratingListRepository: QagModeratingListRepository,
) {
    fun getQagModeratingList(userId: String): List<QagModerating> {
        return qagModeratingListRepository.getQagModeratingList().mapNotNull { qagModeratingInfo ->
            thematiqueRepository.getThematique(qagModeratingInfo.thematiqueId)?.let { thematique ->
                supportRepository.getSupportQag(
                    qagId = qagModeratingInfo.id,
                    userId = userId
                )?.let { supportQag ->
                    QagModerating(
                        id = qagModeratingInfo.id,
                        thematique = thematique,
                        title = qagModeratingInfo.title,
                        description = qagModeratingInfo.description,
                        username = qagModeratingInfo.username,
                        date = qagModeratingInfo.date,
                        support = supportQag,
                    )
                }
            }
        }.sortedBy { it.date }
    }

    fun getModeratingQagCount(): Int {
        return qagModeratingListRepository.getModeratingQagCount()
    }
}
