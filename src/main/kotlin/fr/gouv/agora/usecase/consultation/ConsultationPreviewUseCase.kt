package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewOngoing
import fr.gouv.agora.domain.ConsultationPreviewPage
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class ConsultationPreviewUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val ongoingMapper: ConsultationPreviewOngoingMapper,
    private val finishedMapper: ConsultationPreviewFinishedMapper,
    private val cacheRepository: ConsultationPreviewPageRepository,
) {

    fun getConsultationPreviewPage(userId: String): ConsultationPreviewPage {
        val ongoingList = cacheRepository.getConsultationPreviewOngoingList()
        val finishedList = cacheRepository.getConsultationPreviewFinishedList()
        val answeredList = cacheRepository.getConsultationPreviewAnsweredList(userId)
            ?: buildAnsweredList(userId)

        if (ongoingList != null && finishedList != null) {
            return ConsultationPreviewPage(
                ongoingList = ongoingList.removeAnsweredConsultation(answeredList),
                finishedList = finishedList,
                answeredList = answeredList,
            )
        }

        return ConsultationPreviewPage(
            ongoingList = (ongoingList ?: buildOngoingList()).removeAnsweredConsultation(answeredList),
            finishedList = finishedList ?: buildFinishedList(),
            answeredList = answeredList,
        )
    }

    private fun buildOngoingList(): List<ConsultationPreviewOngoing> {
        return consultationInfoRepository.getOngoingConsultations()
            .mapNotNull { consultationInfo ->
                thematiqueRepository.getThematique(consultationInfo.thematiqueId)
                    ?.let { ongoingMapper.toConsultationPreviewOngoing(consultationInfo, it) }
            }.also { ongoingList ->
                cacheRepository.insertConsultationPreviewOngoingList(ongoingList)
            }
    }

    private fun buildFinishedList(): List<ConsultationPreviewFinished> {
        return consultationInfoRepository.getFinishedConsultations().mapNotNull { consultationWithUpdateInfo ->
            thematiqueRepository.getThematique(consultationWithUpdateInfo.thematiqueId)
                ?.let { thematique ->
                    finishedMapper.toConsultationPreviewFinished(
                        consultationInfo = consultationWithUpdateInfo,
                        thematique = thematique,
                    )
                }
        }.also { finishedList ->
            cacheRepository.insertConsultationPreviewFinishedList(finishedList)
        }
    }

    private fun buildAnsweredList(userId: String): List<ConsultationPreviewFinished> {
        val answeredConsultationsWithThematique = consultationInfoRepository.getAnsweredConsultations(userId)
            .mapNotNull { consultationWithUpdateInfo ->
                thematiqueRepository.getThematique(consultationWithUpdateInfo.thematiqueId)
                    ?.let { finishedMapper.toConsultationPreviewFinished(consultationWithUpdateInfo, it) }
            }
        cacheRepository.insertConsultationPreviewAnsweredList(userId, answeredConsultationsWithThematique)

        return answeredConsultationsWithThematique
    }

    private fun List<ConsultationPreviewOngoing>.removeAnsweredConsultation(answeredList: List<ConsultationPreviewFinished>): List<ConsultationPreviewOngoing> {
        return this.filterNot { ongoingConsultation ->
            answeredList.any { answeredConsultation -> answeredConsultation.id == ongoingConsultation.id }
        }
    }

}
