package fr.gouv.agora.usecase.consultationPaginated

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewFinishedInfo
import fr.gouv.agora.usecase.consultation.ConsultationPreviewFinishedMapper
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewFinishedRepository
import fr.gouv.agora.usecase.consultationUpdate.ConsultationUpdateUseCase
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class GetConsultationFinishedPaginatedListUseCase(
    private val consultationPreviewFinishedRepository: ConsultationPreviewFinishedRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val mapper: ConsultationPreviewFinishedMapper,
    private val consultationUpdateUseCase: ConsultationUpdateUseCase,
) {
    companion object {
        private const val MAX_PAGE_LIST_SIZE = 20
    }

    fun getConsultationFinishedPaginatedList(pageNumber: Int): ConsultationFinishedPaginatedList? {
        if (pageNumber <= 0) return null
        val consultationFinishedList = consultationPreviewFinishedRepository.getConsultationFinishedList()

        val minIndex = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        if (minIndex > consultationFinishedList.size) return null
        val maxIndex = Integer.min(pageNumber * MAX_PAGE_LIST_SIZE, consultationFinishedList.size)

        val consultationFinishedPaginatedList = consultationFinishedList
            .subList(fromIndex = minIndex, toIndex = maxIndex)

        return ConsultationFinishedPaginatedList(
            consultationFinishedList = getConsultationFinishedPreviewList(consultationFinishedPaginatedList),
            maxPageNumber = ceil(consultationFinishedList.size.toDouble() / MAX_PAGE_LIST_SIZE.toDouble()).toInt(),
        )
    }

    private fun getConsultationFinishedPreviewList(consultationFinishedList: List<ConsultationPreviewFinishedInfo>): List<ConsultationPreviewFinished> {
        val consultationUpdateList = consultationUpdateUseCase.getConsultationPreviewUpdates(consultationFinishedList)
        val thematiqueList = thematiqueRepository.getThematiqueList()
        return consultationFinishedList.mapNotNull { consultationPreviewFinishedInfo ->
                    consultationUpdateList.find { consultationUpdate -> consultationPreviewFinishedInfo.id == consultationUpdate.consultationId }
                        ?.let { consultationUpdate ->
                            thematiqueList.find { thematique -> consultationPreviewFinishedInfo.thematiqueId == thematique.id }
                                ?.let { thematique ->
                                    mapper.toConsultationPreviewFinished(
                                        consultationPreviewInfo = consultationPreviewFinishedInfo,
                                        consultationUpdate = consultationUpdate,
                                        thematique = thematique,
                                    )
                                }
                        }
        }
    }
}

data class ConsultationFinishedPaginatedList(
    val consultationFinishedList: List<ConsultationPreviewFinished>,
    val maxPageNumber: Int,
)
