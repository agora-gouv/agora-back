package fr.social.gouv.agora.infrastructure.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultation
import fr.social.gouv.agora.usecase.reponseConsultation.repository.ReponseConsultationRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class ReponseConsultationRepositoryImpl(
    private val databaseRepository: ReponseConsultationDatabaseRepository,
    private val reponseConsultationMapper: ReponseConsultationMapper,
) : ReponseConsultationRepository {


    override fun insertReponseConsultation(reponseConsultation: ReponseConsultation): InsertStatus {
        val reponsesConsultationDTO = reponseConsultationMapper.toDto(reponseConsultation)
        println(reponsesConsultationDTO)
        val existInDatabase = databaseRepository.existsById(UUID.fromString(reponseConsultation.id))
        return if (existInDatabase)
            InsertStatus.INSERT_CONFLICT
        else {
            for (reponseConsultationDTO in reponsesConsultationDTO)
                databaseRepository.save(reponseConsultationDTO)
            InsertStatus.INSERT_SUCCESS
        }
    }
}

enum class InsertStatus {
    INSERT_SUCCESS, INSERT_CONFLICT
}