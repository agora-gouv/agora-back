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
    private val finishedMapper: ConsultationPreviewFinishedMapper,
    private val cacheRepository: ConsultationPreviewPageRepository,
) {

    fun getConsultationPreviewPage(userId: String): ConsultationPreviewPage {
        val cachedOngoingConsultations = cacheRepository.getConsultationPreviewOngoingList()
        val cachedFinishedConsultations = cacheRepository.getConsultationPreviewFinishedList()
        val answeredList = cacheRepository.getConsultationPreviewAnsweredList(userId)
            ?: buildAnsweredList(userId)

        if (cachedOngoingConsultations != null && cachedFinishedConsultations != null) {
            return ConsultationPreviewPage(
                ongoingList = cachedOngoingConsultations.removeAnsweredConsultation(answeredList),
                finishedList = cachedFinishedConsultations,
                answeredList = answeredList,
            )
        }

        return ConsultationPreviewPage(
            ongoingList = (cachedOngoingConsultations ?: getOngoingConsultationsAndCacheThem()).removeAnsweredConsultation(answeredList),
            finishedList = cachedFinishedConsultations ?: buildFinishedList(),
            answeredList = answeredList,
        )
    }

    private fun getOngoingConsultationsAndCacheThem(): List<ConsultationPreviewOngoing> {
        val consultations = consultationInfoRepository.getOngoingConsultations()
            .sortedBy { it.endDate }

        cacheRepository.insertConsultationPreviewOngoingList(consultations)

        return consultations
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
