package fr.gouv.agora.infrastructure.consultationResponse.repository

import fr.gouv.agora.domain.ReponseConsultationInserting
import fr.gouv.agora.usecase.consultationResponse.repository.InsertReponseConsultationRepository
import fr.gouv.agora.usecase.consultationResponse.repository.InsertReponseConsultationRepository.InsertParameters
import fr.gouv.agora.usecase.consultationResponse.repository.InsertReponseConsultationRepository.InsertResult
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
            val userUUID = UUID.fromString(insertParameters.userId)
            val participationUUID = UUID.fromString(insertParameters.participationId)

            consultationResponses.flatMap { consultationResponse ->
                mapper.toDto(
                    consultationId = insertParameters.consultationId,
                    userId = userUUID,
                    participationId = participationUUID,
                    domain = consultationResponse
                )
            }.takeUnless { it.isEmpty() }?.let { dtoList ->
                val savedConsultationResponseDTOList = databaseRepository.saveAll(dtoList)
                cacheRepository.insertReponseConsultationList(
                    consultationId = insertParameters.consultationId,
                    reponseConsultationList = savedConsultationResponseDTOList.toList(),
                )
            }
            InsertResult.INSERT_SUCCESS
        } catch (e: IllegalArgumentException) {
            InsertResult.INSERT_FAILURE
        }
    }
}
