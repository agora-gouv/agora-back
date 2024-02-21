package fr.gouv.agora.usecase.consultationUpdate.repository

import fr.gouv.agora.domain.ConsultationUpdateHistory

interface ConsultationUpdateHistoryRepository {
    fun getConsultationUpdateHistory(consultationId: String): List<ConsultationUpdateHistory>
}