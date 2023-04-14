package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.usecase.reponseConsultation.repository.ReponseConsultationRepository
import org.springframework.stereotype.Component

@Component
class ReponseConsultationRepositoryImpl(
    private val databaseRepository: ReponseConsultationDatabaseRepository,
    private val reponseConsultationMapper: ReponseConsultationMapper,
) : ReponseConsultationRepository {


    override fun insertReponseConsultation(reponseConsultation: ReponseConsultationInserting): InsertStatus {
        val reponseConsultationDTOList = reponseConsultationMapper.toDto(reponseConsultation)
        for (reponseConsultationDTO in reponseConsultationDTOList) {
            if (!databaseRepository.existsById(reponseConsultationDTO.id))
                databaseRepository.save(reponseConsultationDTO)
            else
                return InsertStatus.INSERT_CONFLICT
        }
        return InsertStatus.INSERT_SUCCESS
    }
}

enum class InsertStatus {
    INSERT_SUCCESS, INSERT_CONFLICT
}