package fr.gouv.agora.oninit

import fr.gouv.agora.usecase.consultation.ConsultationCacheClearUseCase
import fr.gouv.agora.usecase.consultationAggregate.PickConsultationsToAggregateUseCase
import fr.gouv.agora.usecase.suspiciousUser.SuspiciousActivityDetectUseCase
import org.springframework.stereotype.Component

@Component
class DailyTasksHandler(
    private val consultationCacheClearUseCase: ConsultationCacheClearUseCase,
    private val pickConsultationsToAggregateUseCase: PickConsultationsToAggregateUseCase,
    private val suspiciousActivityDetectUseCase: SuspiciousActivityDetectUseCase,
) : CustomCommandHandler {

    override fun handleTask(arguments: Map<String, String>?) {
        consultationCacheClearUseCase.clearConsultationCaches()
        pickConsultationsToAggregateUseCase.aggregateConsultations()
        suspiciousActivityDetectUseCase.flagSuspiciousUsers()
        // TODOs
        // - Delete everything related to a user when last connection date is over 2 years (except QaG if status is SELECTED_FOR_RESPONSE)
        // - Remove feedback link to userId ?
    }

}