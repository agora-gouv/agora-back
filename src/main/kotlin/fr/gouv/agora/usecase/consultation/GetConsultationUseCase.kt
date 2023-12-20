package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.Consultation
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service


@Service
class GetConsultationUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val userAnsweredConsultationRepository: UserAnsweredConsultationRepository,
) {
    fun getConsultation(consultationId: String, userId: String): Consultation? {
        return consultationInfoRepository.getConsultation(consultationId)?.let { consultationInfo ->
            thematiqueRepository.getThematique(consultationInfo.thematiqueId)?.let { thematique ->
                Consultation(
                    id = consultationInfo.id,
                    title = consultationInfo.title,
                    coverUrl = consultationInfo.coverUrl,
                    startDate = consultationInfo.startDate,
                    endDate = consultationInfo.endDate,
                    questionCount = consultationInfo.questionCount,
                    estimatedTime = consultationInfo.estimatedTime,
                    participantCountGoal = consultationInfo.participantCountGoal,
                    description = consultationInfo.description,
                    tipsDescription = consultationInfo.tipsDescription,
                    thematique = thematique,
                    hasAnswered = userAnsweredConsultationRepository.hasAnsweredConsultation(
                        consultationId = consultationId,
                        userId = userId,
                    )
                )
            }
        }
    }
}
