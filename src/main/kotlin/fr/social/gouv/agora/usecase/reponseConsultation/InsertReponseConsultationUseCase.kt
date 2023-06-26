package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewAnsweredRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertResult
import org.springframework.stereotype.Service

@Service
class InsertReponseConsultationUseCase(
    private val consultationPreviewAnsweredRepository: ConsultationPreviewAnsweredRepository,
    private val insertReponseConsultationRepository: InsertReponseConsultationRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
    private val insertConsultationResponseParametersMapper: InsertConsultationResponseParametersMapper,
) {

    fun insertReponseConsultation(
        consultationId: String,
        userId: String,
        consultationResponses: List<ReponseConsultationInserting>,
    ): InsertResult {
        if (consultationResponseRepository.hasAnsweredConsultation(consultationId = consultationId, userId = userId)) {
            return InsertResult.INSERT_FAILURE
        }

        consultationPreviewAnsweredRepository.deleteConsultationAnsweredListFromCache(userId)
        return insertReponseConsultationRepository.insertConsultationResponses(
            insertParameters = insertConsultationResponseParametersMapper.toInsertParameters(
                consultationId = consultationId,
                userId = userId,
            ),
            consultationResponses = consultationResponses,
        )
    }
}
