package fr.gouv.agora.infrastructure.reponseConsultation.repository

import fr.gouv.agora.domain.ReponseConsultationInserting
import fr.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository
import fr.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertParameters
import fr.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertResult
import org.springframework.stereotype.Component
import java.util.*

@Component
class InsertReponseConsultationRepositoryImpl(
    private val databaseRepository: ReponseConsultationDatabaseRepository,
    private val cacheRepository: ReponseConsultationCacheRepository,
    private val mapper: ReponseConsultationMapper,
) : InsertReponseConsultationRepository {

    override fun insertConsultationResponses(
        insertParameters: InsertParameters,
        consultationResponses: List<ReponseConsultationInserting>
    ): InsertResult {
        return try {
            val consultationUUID = UUID.fromString(insertParameters.consultationId)
            val userUUID = UUID.fromString(insertParameters.userId)
            val participationUUID = UUID.fromString(insertParameters.participationId)

            consultationResponses.flatMap { consultationResponse ->
                mapper.toDto(
                    consultationId = consultationUUID,
                    userId = userUUID,
                    participationId = participationUUID,
                    domain = consultationResponse
                )
            }.takeUnless { it.isEmpty() }?.let { dtoList ->
                val savedConsultationResponseDTOList = databaseRepository.saveAll(dtoList)
                cacheRepository.insertReponseConsultationList(
                    consultationId = consultationUUID,
                    reponseConsultationList = savedConsultationResponseDTOList.toList(),
                )
            }
            InsertResult.INSERT_SUCCESS
        } catch (e: IllegalArgumentException) {
            InsertResult.INSERT_FAILURE
        }
    }
}

