package fr.gouv.agora.usecase.consultationResults

import fr.gouv.agora.domain.ConsultationResultsWithUpdate
import fr.gouv.agora.usecase.consultationUpdate.ConsultationUpdateUseCase
import org.springframework.stereotype.Service

@Service
class ConsultationResultsWithUpdateUseCase(
    private val consultationResultsUseCase: ConsultationResultsUseCase,
    private val consultationUpdateUseCase: ConsultationUpdateUseCase,
) {

    fun getConsultationResults(consultationId: String): ConsultationResultsWithUpdate? {
        return consultationResultsUseCase.getConsultationResults(consultationId = consultationId)?.let { results ->
            consultationUpdateUseCase.getConsultationUpdate(results.consultation)?.let { update ->
                ConsultationResultsWithUpdate(
                    consultation = results.consultation,
                    lastUpdate = update,
                    participantCount = results.participantCount,
                    results = results.results,
                )
            }
        }
    }

}