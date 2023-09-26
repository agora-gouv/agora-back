package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationStatus
import fr.social.gouv.agora.domain.ConsultationUpdate
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.social.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateRepository
import fr.social.gouv.agora.usecase.reponseConsultation.repository.GetConsultationResponseRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
class ConsultationListUseCase(
    private val clock: Clock,
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val consultationUpdateRepository: ConsultationUpdateRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
) {

    fun getConsultationList(userId: String): List<ConsultationWithThematiqueUpdateAndAnswered> {
        // TODO : tests
        val dateNow = LocalDateTime.now(clock)
        val consultationInfoList = consultationInfoRepository.getConsultations()
        val consultationInfoIds = consultationInfoList.map { consultationInfo -> consultationInfo.id }

        val thematiqueList = thematiqueRepository.getThematiqueList()
        val consultationUpdates = consultationUpdateRepository.getConsultationUpdates(consultationInfoIds)
        val hasAnswered = consultationResponseRepository.hasAnsweredConsultations(consultationInfoIds, userId)

        return consultationInfoList.mapNotNull { consultationInfo ->
            val thematique = thematiqueList.find { thematique -> consultationInfo.thematiqueId == thematique.id }
            val consultationUpdate = if (isOngoing(dateNow, consultationInfo)) {
                consultationUpdates.find { consultationUpdate -> consultationUpdate.consultationId == consultationInfo.id && consultationUpdate.status == ConsultationStatus.COLLECTING_DATA }
            } else {
                consultationUpdates.find { consultationUpdate -> consultationUpdate.consultationId == consultationInfo.id && consultationUpdate.status != ConsultationStatus.COLLECTING_DATA }
            }

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

    private fun isOngoing(dateNow: LocalDateTime, consultationInfo: ConsultationInfo) =
        isOngoing(
            dateNow = dateNow,
            startDate = consultationInfo.startDate.toLocalDateTime(),
            endDate = consultationInfo.endDate.toLocalDateTime(),
        )

    private fun isOngoing(dateNow: LocalDateTime, startDate: LocalDateTime, endDate: LocalDateTime) =
        (dateNow.isAfter(startDate) || dateNow == startDate) && dateNow.isBefore(endDate)

}

data class ConsultationWithThematiqueUpdateAndAnswered(
    val info: ConsultationInfo,
    val thematique: Thematique,
    val update: ConsultationUpdate,
    val hasAnswered: Boolean,
)