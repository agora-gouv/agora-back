package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.config.AuthentificationHelper
import fr.gouv.agora.domain.ConsultationPreview
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewPage
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import org.springframework.stereotype.Service

@Service
class ConsultationPreviewUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val cacheRepository: ConsultationPreviewPageRepository,
    private val authentificationHelper: AuthentificationHelper,
) {
    fun getConsultationPreviewPage(userId: String): ConsultationPreviewPage {
        val cachedOngoingConsultations = cacheRepository.getConsultationPreviewOngoingList()
        val cachedFinishedConsultations = cacheRepository.getConsultationPreviewFinishedList()
        val answeredList = cacheRepository.getConsultationPreviewAnsweredList(userId)
            ?: buildAnsweredList(userId)

        if (authentificationHelper.canViewUnpublishedConsultations()) {
            val onGoingConsultations = consultationInfoRepository.getOngoingConsultationsWithUnpublished()
                .sortedBy { it.endDate }
            val finishedConsultations = consultationInfoRepository.getFinishedConsultationsWithUnpublished()
            val answeredConsultations = consultationInfoRepository.getAnsweredConsultations(userId)

            return ConsultationPreviewPage(
                ongoingList = onGoingConsultations.removeAnsweredConsultation(answeredConsultations),
                finishedList = finishedConsultations,
                answeredList = answeredConsultations,
            )
        }

        return ConsultationPreviewPage(
            ongoingList = (cachedOngoingConsultations
                ?: getOngoingConsultationsAndCacheThem()).removeAnsweredConsultation(answeredList),
            finishedList = cachedFinishedConsultations ?: buildFinishedList(),
            answeredList = answeredList,
        )
    }

    private fun getOngoingConsultationsAndCacheThem(): List<ConsultationPreview> {
        return consultationInfoRepository.getOngoingConsultationsWithUnpublished()
            .sortedBy { it.endDate }
            .filter { it.isPublished }
            .also { cacheRepository.insertConsultationPreviewOngoingList(it) }
    }

    private fun buildFinishedList(): List<ConsultationPreviewFinished> {
        return consultationInfoRepository.getFinishedConsultationsWithUnpublished()
            .filter { it.isPublished }
            .also { finishedList -> cacheRepository.insertConsultationPreviewFinishedList(finishedList) }
    }

    private fun buildAnsweredList(userId: String): List<ConsultationPreviewFinished> {
        val answeredConsultationsWithThematique = consultationInfoRepository.getAnsweredConsultations(userId)
        cacheRepository.insertConsultationPreviewAnsweredList(userId, answeredConsultationsWithThematique)

        return answeredConsultationsWithThematique
    }

    private fun List<ConsultationPreview>.removeAnsweredConsultation(answeredList: List<ConsultationPreviewFinished>): List<ConsultationPreview> {
        return this.filterNot { ongoingConsultation ->
            answeredList.any { answeredConsultation -> answeredConsultation.id == ongoingConsultation.id }
        }
    }
}
