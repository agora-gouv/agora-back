package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreviewAnswered
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewOngoing
import fr.gouv.agora.domain.ConsultationPreviewPage
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfoRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class ConsultationPreviewUseCase(
    private val consultationInfoRepository: ConsultationInfoRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val ongoingMapper: ConsultationPreviewOngoingMapper,
    private val finishedMapper: ConsultationPreviewFinishedMapper,
    private val cacheRepository: ConsultationPreviewPageRepository,
) {

    fun getConsultationPreviewPage(userId: String): ConsultationPreviewPage {
        val ongoingList = cacheRepository.getConsultationPreviewOngoingList()
        val finishedList = cacheRepository.getConsultationPreviewFinishedList()
        val answeredList = cacheRepository.getConsultationPreviewAnsweredList(userId)
            ?: buildAnsweredList(userId)

        if (ongoingList != null && finishedList != null) {
            return ConsultationPreviewPage(
                ongoingList = ongoingList,
                finishedList = finishedList,
                answeredList = answeredList,
            )
        }

        return ConsultationPreviewPage(
            ongoingList = (ongoingList ?: buildOngoingList()).filterNot { ongoingConsultation ->
                answeredList.any { answeredConsultation -> answeredConsultation.id == ongoingConsultation.id }
            },
            finishedList = finishedList ?: buildFinishedList(),
            answeredList = answeredList,
        )
    }

    private fun buildOngoingList(): List<ConsultationPreviewOngoing> {
        val thematiqueList = thematiqueRepository.getThematiqueList()
        return consultationInfoRepository.getOngoingConsultations()
            .mapNotNull { consultationInfo ->
                thematiqueList.find { thematique -> consultationInfo.thematiqueId == thematique.id }
                    ?.let { thematique ->
                        ongoingMapper.toConsultationPreviewOngoing(
                            consultationInfo = consultationInfo,
                            thematique = thematique,
                        )
                    }
            }.also { ongoingList ->
                cacheRepository.insertConsultationPreviewOngoingList(ongoingList)
            }
    }

    private fun buildFinishedList(): List<ConsultationPreviewFinished> {
        val thematiqueList = thematiqueRepository.getThematiqueList()
        return consultationInfoRepository.getFinishedConsultations().mapNotNull { consultationWithUpdateInfo ->
            thematiqueList.find { thematique -> consultationWithUpdateInfo.thematiqueId == thematique.id }
                ?.let { thematique ->
                    finishedMapper.toConsultationPreviewFinished(
                        consultationInfo = consultationWithUpdateInfo,
                        thematique = thematique,
                    )
                }
        }.also { finishedList ->
            cacheRepository.insertConsultationPreviewFinishedList(finishedList)
        }
    }

    private fun buildAnsweredList(userId: String): List<ConsultationPreviewAnswered> {
        val thematiqueList = thematiqueRepository.getThematiqueList()
        return consultationInfoRepository.getAnsweredConsultations(userId).mapNotNull { consultationWithUpdateInfo ->
            thematiqueList.find { thematique -> consultationWithUpdateInfo.thematiqueId == thematique.id }
                ?.let { thematique ->
                    finishedMapper.toConsultationPreviewAnswered(
                        consultationInfo = consultationWithUpdateInfo,
                        thematique = thematique,
                    )
                }
        }.also { answeredList ->
            cacheRepository.insertConsultationPreviewAnsweredList(userId, answeredList)
        }
    }

}
