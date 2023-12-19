package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationUpdate
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultationUpdate.ConsultationUpdateUseCase
import fr.gouv.agora.usecase.reponseConsultation.repository.UserAnsweredConsultationRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class ConsultationListUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val userAnsweredConsultationRepository: UserAnsweredConsultationRepository,
    private val consultationUpdateUseCase: ConsultationUpdateUseCase,
) {

    fun getConsultationList(userId: String): List<ConsultationWithThematiqueUpdateAndAnswered> {
        // TODO : tests
        val consultationInfoList = consultationInfoRepository.getConsultations()
        val consultationInfoIds = consultationInfoList.map { consultationInfo -> consultationInfo.id }
        val thematiqueList = thematiqueRepository.getThematiqueList()
        val hasAnswered = userAnsweredConsultationRepository.hasAnsweredConsultations(consultationInfoIds, userId)

        return consultationInfoList.mapNotNull { consultationInfo ->
            val thematique = thematiqueList.find { thematique -> consultationInfo.thematiqueId == thematique.id }
            val consultationUpdate = consultationUpdateUseCase.getConsultationUpdate(consultationInfo)
            if (thematique != null && consultationUpdate != null) {
                ConsultationWithThematiqueUpdateAndAnswered(
                    info = consultationInfo,
                    thematique = thematique,
                    update = consultationUpdate,
                    hasAnswered = hasAnswered[consultationInfo.id] ?: false,
                )
            } else null
        }
    }
}

data class ConsultationWithThematiqueUpdateAndAnswered(
    val info: ConsultationInfo,
    val thematique: Thematique,
    val update: ConsultationUpdate,
    val hasAnswered: Boolean,
)