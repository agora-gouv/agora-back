package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewAnsweredRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertParameters
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertResult
import org.springframework.stereotype.Service
import java.util.*

@Service
class InsertReponseConsultationUseCase(
    private val consultationPreviewAnsweredRepository: ConsultationPreviewAnsweredRepository,
    private val insertReponseConsultationRepository: InsertReponseConsultationRepository,
) {

    fun insertReponseConsultation(
        consultationId: String,
        userId: String,
        consultationResponses: List<ReponseConsultationInserting>,
    ): InsertResult {
        consultationPreviewAnsweredRepository.deleteConsultationAnsweredList(userId)
        return insertReponseConsultationRepository.insertConsultationResponses(
            insertParameters = InsertParameters(
                consultationId = consultationId,
                userId = userId,
                participationId = UUID.randomUUID().toString(),
            ),
            consultationResponses = consultationResponses,
        )
    }
}
