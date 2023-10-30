package fr.social.gouv.agora.usecase.consultation

import fr.social.gouv.agora.domain.ConsultationPreviewAnswered
import fr.social.gouv.agora.domain.ConsultationPreviewFinished
import fr.social.gouv.agora.domain.ConsultationPreviewOngoing
import fr.social.gouv.agora.domain.ConsultationPreviewPage
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.LocalDateTime

@Service
class ConsultationPreviewUseCase(
    private val clock: Clock,
    private val consultationListUseCase: ConsultationListUseCase,
    private val ongoingMapper: ConsultationPreviewOngoingMapper,
    private val finishedMapper: ConsultationPreviewFinishedMapper,
    private val answeredMapper: ConsultationPreviewAnsweredMapper,
    private val consultationPreviewPageRepository: ConsultationPreviewPageRepository,
) {

    fun getConsultationPreviewPage(userId: String): ConsultationPreviewPage {
        // TODO: tests
        val ongoingList = consultationPreviewPageRepository.getConsultationPreviewOngoingList(userId)
        val finishedList = consultationPreviewPageRepository.getConsultationPreviewFinishedList()
        val answeredList = consultationPreviewPageRepository.getConsultationPreviewAnsweredList(userId)

        if (ongoingList != null && finishedList != null && answeredList != null) {
            return ConsultationPreviewPage(
                ongoingList = ongoingList,
                finishedList = finishedList,
                answeredList = answeredList,
            )
        }

        val dateNow = LocalDateTime.now(clock)
        val consultations = consultationListUseCase.getConsultationList(userId)
        return ConsultationPreviewPage(
            ongoingList = ongoingList ?: buildOngoingList(userId, dateNow, consultations),
            finishedList = finishedList ?: buildFinishedList(dateNow, consultations).takeIf { it.isNotEmpty() }
            ?: buildFinishedFromOngoingList(dateNow, consultations),
            answeredList = answeredList ?: buildAnsweredList(userId, consultations),
        )
    }

    private fun buildOngoingList(
        userId: String,
        dateNow: LocalDateTime,
        consultations: List<ConsultationWithThematiqueUpdateAndAnswered>,
    ): List<ConsultationPreviewOngoing> {
        return consultations
            .filter { consultation -> isOngoing(dateNow, consultation.info) && !consultation.hasAnswered }
            .map { consultation ->
                ongoingMapper.toConsultationPreviewOngoing(consultation.info, consultation.thematique)
            }
            .also { consultationPreviewPageRepository.insertConsultationPreviewOngoingList(userId, it) }
    }

    private fun buildFinishedList(
        dateNow: LocalDateTime,
        consultations: List<ConsultationWithThematiqueUpdateAndAnswered>,
    ): List<ConsultationPreviewFinished> {
        return consultations
            .filter { consultation -> isFinished(dateNow, consultation.info) }
            .map { consultation ->
                finishedMapper.toConsultationPreviewFinished(
                    consultationInfo = consultation.info,
                    consultationUpdate = consultation.update,
                    thematique = consultation.thematique
                )
            }
            .also { consultationPreviewPageRepository.insertConsultationPreviewFinishedList(it) }
    }

    private fun buildFinishedFromOngoingList(
        dateNow: LocalDateTime,
        consultations: List<ConsultationWithThematiqueUpdateAndAnswered>,
    ): List<ConsultationPreviewFinished> {
        return consultations
            .filter { consultation -> isOngoing(dateNow, consultation.info) }
            .map { consultation ->
                finishedMapper.toConsultationPreviewFinished(
                    consultationInfo = consultation.info,
                    consultationUpdate = consultation.update,
                    thematique = consultation.thematique,
                )
            }
            .also { consultationPreviewPageRepository.insertConsultationPreviewFinishedList(it) }
    }

    private fun buildAnsweredList(
        userId: String,
        consultations: List<ConsultationWithThematiqueUpdateAndAnswered>,
    ): List<ConsultationPreviewAnswered> {
        return consultations
            .filter { consultation -> consultation.hasAnswered }
            .map { consultation ->
                answeredMapper.toConsultationPreviewAnswered(
                    consultationInfo = consultation.info,
                    consultationUpdate = consultation.update,
                    thematique = consultation.thematique,
                )
            }
            .also { consultationPreviewPageRepository.insertConsultationPreviewAnsweredList(userId, it) }
    }

    private fun isOngoing(dateNow: LocalDateTime, consultationInfo: ConsultationInfo) =
        isOngoing(
            dateNow = dateNow,
            startDate = consultationInfo.startDate.toLocalDateTime(),
            endDate = consultationInfo.endDate.toLocalDateTime(),
        )

    private fun isOngoing(dateNow: LocalDateTime, startDate: LocalDateTime, endDate: LocalDateTime) =
        (dateNow.isAfter(startDate) || dateNow == startDate) && dateNow.isBefore(endDate)

    private fun isFinished(dateNow: LocalDateTime, consultationInfo: ConsultationInfo) =
        isFinished(dateNow = dateNow, endDate = consultationInfo.endDate.toLocalDateTime())

    private fun isFinished(dateNow: LocalDateTime, endDate: LocalDateTime) =
        dateNow == endDate || dateNow.isAfter(endDate)

}
