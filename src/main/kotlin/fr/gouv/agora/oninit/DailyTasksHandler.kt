package fr.gouv.agora.oninit

import fr.gouv.agora.usecase.consultation.ConsultationCacheClearUseCase
import fr.gouv.agora.usecase.consultationAggregate.PickConsultationsToAggregateUseCase
import org.springframework.stereotype.Component

@Component
class DailyTasksHandler(
    private val consultationCacheClearUseCase: ConsultationCacheClearUseCase,
    private val pickConsultationsToAggregateUseCase: PickConsultationsToAggregateUseCase,
) : CustomCommandHandler {

    override fun handleTask(arguments: Map<String, String>?) {
        consultationCacheClearUseCase.clearConsultationCaches()
        pickConsultationsToAggregateUseCase.aggregateConsultations()
        // TODOs
        // - Consultation responses aggregation when finished
        // - Save consultations answered by users but remove link to responses
        // - Delete everything related to a user when last connection date is over 2 years (except QaG if status is SELECTED_FOR_RESPONSE)
        // - Remove feedback link to userId ?
    }

}