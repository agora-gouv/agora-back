package fr.social.gouv.agora.usecase.reponseConsultation

import fr.social.gouv.agora.domain.ReponseConsultationInserting
import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertParameters
import fr.social.gouv.agora.usecase.reponseConsultation.repository.InsertReponseConsultationRepository.InsertResult
import org.springframework.stereotype.Service
import java.util.*

@Service
class InsertReponseConsultationUseCase(
    private val loginRepository: LoginRepository,
    private val insertReponseConsultationRepository: InsertReponseConsultationRepository,
) {

    fun insertReponseConsultation(
        consultationId: String,
        deviceId: String,
        consultationResponses: List<ReponseConsultationInserting>
    ): InsertResult {
        return loginRepository.getUser(deviceId = deviceId)?.let { userInfo ->
            insertReponseConsultationRepository.insertConsultationResponses(
                insertParameters = InsertParameters(
                    consultationId = consultationId,
                    userId = userInfo.userId,
                    participationId = UUID.randomUUID().toString(),
                ),
                consultationResponses = consultationResponses,
            )
        } ?: InsertResult.INSERT_FAILURE
    }

}