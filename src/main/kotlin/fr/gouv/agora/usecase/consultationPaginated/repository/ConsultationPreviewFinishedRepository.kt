package fr.gouv.agora.usecase.consultationPaginated.repository

import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.usecase.consultation.repository.ConsultationWithUpdateInfo

interface ConsultationPreviewFinishedRepository {
    fun getConsultationFinishedCount(): Int
    fun getConsultationFinishedList(offset: Int, pageSize: Int, territory: Territoire): List<ConsultationWithUpdateInfo>
    fun getConsultationFinishedList(
        territories: List<Territoire>,
    ): List<ConsultationWithUpdateInfo>
}
