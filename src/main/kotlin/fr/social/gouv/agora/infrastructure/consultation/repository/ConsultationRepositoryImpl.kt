package fr.social.gouv.agora.infrastructure.consultation.repository

import fr.social.gouv.agora.domain.Consultation
import fr.social.gouv.agora.usecase.consultation.ConsultationRepository
import org.springframework.cache.CacheManager

class ConsultationRepositoryImpl(
    private val databaseRepository: ConsultationDatabaseRepository,
    private val cacheManager: CacheManager,
) : ConsultationRepository {
    override fun getConsultation(id: String): Consultation {
        TODO("Not yet implemented")
    }
}