package fr.social.gouv.agora.usecase.reponseConsultation.repository

import fr.social.gouv.agora.domain.ReponseConsultationInserting

interface InsertReponseConsultationRepository {

    enum class InsertResult {
        INSERT_SUCCESS, INSERT_FAILURE
    }

    data class InsertParameters(
        val consultationId: String,
        val userId: String,
        val participationId: String,
    )

    fun insertConsultationResponses(
        insertParameters: InsertParameters,
        consultationResponses: List<ReponseConsultationInserting>
    ): InsertResult

}
