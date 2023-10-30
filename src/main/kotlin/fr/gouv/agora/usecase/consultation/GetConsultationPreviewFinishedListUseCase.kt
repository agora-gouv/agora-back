package fr.gouv.agora.usecase.consultation

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewInfo
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.utils.CollectionUtils.mapNotNullWhile
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewFinishedRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewOngoingRepository
import fr.gouv.agora.usecase.consultationUpdate.ConsultationUpdateUseCase
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service

@Service
class GetConsultationPreviewFinishedListUseCase(
    private val consultationPreviewFinishedRepository: ConsultationPreviewFinishedRepository,
    private val consultationPreviewOngoingRepository: ConsultationPreviewOngoingRepository,
    private val consultationUpdateUseCase: ConsultationUpdateUseCase,
    private val thematiqueRepository: ThematiqueRepository,
    private val mapper: ConsultationPreviewFinishedMapper,
) {
    companion object {
        private const val MAX_CONSULTATION_FINISHED_LIST_SIZE = 5
    }

    fun getConsultationPreviewFinishedList(): List<ConsultationPreviewFinished> {
        val thematiqueMap = mutableMapOf<String, Thematique?>()
        val consultationList =
            consultationPreviewFinishedRepository.getConsultationFinishedList().takeIf { it.isNotEmpty() }
                ?: consultationPreviewOngoingRepository.getConsultationPreviewOngoingList()

        return consultationList.mapNotNullWhile(
            transformMethod = { consultationInfo ->
                toConsultationPreview(consultationInfo, thematiqueMap)
            },
            loopWhileCondition = { consultationPreviewList -> consultationPreviewList.size < MAX_CONSULTATION_FINISHED_LIST_SIZE }
        ).take(MAX_CONSULTATION_FINISHED_LIST_SIZE)
    }

    private fun toConsultationPreview(
        consultationInfo: ConsultationPreviewInfo,
        thematiqueMap: MutableMap<String, Thematique?>
    ): ConsultationPreviewFinished? {
        return consultationUpdateUseCase.getConsultationUpdate(consultationInfo)
            ?.let { consultationUpdate ->
                getThematique(consultationInfo.thematiqueId, thematiqueMap)
                    ?.let { thematique ->
                        mapper.toConsultationPreviewFinished(
                            consultationPreviewInfo = consultationInfo,
                            consultationUpdate = consultationUpdate,
                            thematique = thematique,
                        )
                    }
            }
    }

    private fun getThematique(
        thematiqueId: String,
        thematiqueMap: MutableMap<String, Thematique?>,
    ): Thematique? {
        return if (thematiqueMap.containsKey(thematiqueId)) {
            thematiqueMap[thematiqueId]
        } else {
            thematiqueRepository.getThematique(thematiqueId).also { thematique ->
                thematiqueMap[thematiqueId] = thematique
            }
        }
    }

}