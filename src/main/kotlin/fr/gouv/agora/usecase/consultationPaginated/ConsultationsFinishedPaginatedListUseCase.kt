package fr.gouv.agora.usecase.consultationPaginated

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.usecase.consultation.ConsultationPreviewFinishedMapper
import fr.gouv.agora.usecase.consultation.repository.ConsultationPreviewFinishedRepository
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationFinishedPaginatedListCacheRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class ConsultationsFinishedPaginatedListUseCase(
    private val consultationPreviewFinishedRepository: ConsultationPreviewFinishedRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val mapper: ConsultationPreviewFinishedMapper,
    private val cacheRepository: ConsultationFinishedPaginatedListCacheRepository,
) {
    companion object {
        private const val MAX_PAGE_LIST_SIZE = 20
    }

    fun getConsultationFinishedPaginatedList(pageNumber: Int): ConsultationFinishedPaginatedList? {
        if (pageNumber <= 0) return null

        cacheRepository.getConsultationFinishedPage(pageNumber = pageNumber)?.let { pageContent ->
            return pageContent
        }

        val consultationsCount = consultationPreviewFinishedRepository.getConsultationFinishedCount()
        val offset = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        if (offset > consultationsCount) return null

        return ConsultationFinishedPaginatedList(
            consultationFinishedList = buildConsultationFinishedPreviewList(offset),
            maxPageNumber = ceil(consultationsCount.toDouble() / MAX_PAGE_LIST_SIZE.toDouble()).toInt(),
        ).also { cacheRepository.initConsultationFinishedPage(pageNumber = pageNumber, content = it) }
    }

    private fun buildConsultationFinishedPreviewList(offset: Int): List<ConsultationPreviewFinished> {
        val thematiqueList = thematiqueRepository.getThematiqueList()
        return consultationPreviewFinishedRepository.getConsultationFinishedList(offset)
            .mapNotNull { consultationInfo ->
                thematiqueList.find { thematique -> consultationInfo.thematiqueId == thematique.id }
                    ?.let { thematique ->
                        mapper.toConsultationPreviewFinished(
                            consultationInfo = consultationInfo,
                            thematique = thematique,
                        )
                    }
            }
    }
}

data class ConsultationFinishedPaginatedList(
    val consultationFinishedList: List<ConsultationPreviewFinished>,
    val maxPageNumber: Int,
)
