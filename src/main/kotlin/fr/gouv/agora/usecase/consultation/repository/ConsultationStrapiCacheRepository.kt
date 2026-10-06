package fr.gouv.agora.usecase.consultation.repository

import fr.gouv.agora.domain.Territoire
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO

interface ConsultationStrapiCacheRepository {
    fun getOngoingConsultations(territories: List<Territoire>): List<ConsultationStrapiDTO>?
    fun putOngoingConsultations(territories: List<Territoire>, data: List<ConsultationStrapiDTO>)
    fun evictOngoingConsultations()

    fun getFinishedConsultations(territories: List<Territoire>): List<ConsultationStrapiDTO>?
    fun putFinishedConsultations(territories: List<Territoire>, data: List<ConsultationStrapiDTO>)
    fun evictFinishedConsultations()
}
