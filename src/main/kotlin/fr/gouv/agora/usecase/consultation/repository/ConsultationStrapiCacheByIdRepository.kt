package fr.gouv.agora.usecase.consultation.repository

import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO

interface ConsultationStrapiCacheByIdRepository {
    fun getConsultationById(consultationId: String): ConsultationStrapiDTO?
    fun putConsultationById(consultationId: String, dto: ConsultationStrapiDTO?)
    fun getConsultationByIdWithUnpublished(consultationId: String): ConsultationStrapiDTO?
    fun putConsultationByIdWithUnpublished(consultationId: String, dto: ConsultationStrapiDTO?)
    fun evictConsultationById(consultationId: String)
}
