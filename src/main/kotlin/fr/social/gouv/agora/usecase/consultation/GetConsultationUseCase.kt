package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.Consultation
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service


@Service
class GetConsultationUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
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
                    hasAnswered = consultationResponseRepository.getConsultationResponses(consultationId)
                        .any { consultationResponse -> consultationResponse.userId == userId }
                )
            }
        }
    }
}
