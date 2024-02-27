package fr.gouv.agora.oninit

import fr.gouv.agora.usecase.consultation.ConsultationCacheClearUseCase
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import fr.gouv.agora.usecase.consultationResult.PickConsultationsToAggregateUseCase
import org.springframework.stereotype.Component

@Component
class DailyTasksHandler(
    private val consultationCacheClearUseCase: ConsultationCacheClearUseCase,
    private val consultationPreviewPageRepository: ConsultationPreviewPageRepository,
    private val pickConsultationsToAggregateUseCase: PickConsultationsToAggregateUseCase,
    private val consultationDetailsV2CacheRepository: ConsultationDetailsV2CacheRepository,
) : CustomCommandHandler {

    override fun handleTask(arguments: Map<String, String>?) {
        consultationCacheClearUseCase.clearConsultationCaches()
        consultationDetailsV2CacheRepository.clearLastConsultationDetails()
        consultationPreviewPageRepository.clear()
        pickConsultationsToAggregateUseCase.aggregateConsultations()
        // TODOs
        // - Consultation responses aggregation when finished
        // - Save consultations answered by users but remove link to responses
        // - Delete everything related to a user when last connection date is over 2 years (except QaG if status is SELECTED_FOR_RESPONSE)
        // - Remove feedback link to userId ?
    }

}