package fr.gouv.agora.usecase.consultationPaginated

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.usecase.consultation.ConsultationPreviewFinishedMapper
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationAnsweredPaginatedListCacheRepository
import fr.gouv.agora.usecase.consultationPaginated.repository.ConsultationPreviewAnsweredRepository
import fr.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.springframework.stereotype.Service
import kotlin.math.ceil

@Service
class ConsultationsAnsweredPaginatedListUseCase(
    private val consultationPreviewFinishedRepository: ConsultationPreviewAnsweredRepository,
    private val thematiqueRepository: ThematiqueRepository,
    private val mapper: ConsultationPreviewFinishedMapper,
    private val cacheRepository: ConsultationAnsweredPaginatedListCacheRepository,
) {
    companion object {
        private const val MAX_PAGE_LIST_SIZE = 20
    }

    fun getConsultationAnsweredPaginatedList(userId: String, pageNumber: Int): ConsultationAnsweredPaginatedList? {
        if (pageNumber <= 0) return null

        cacheRepository.getConsultationAnsweredPage(userId = userId, pageNumber = pageNumber)?.let { pageContent ->
            return pageContent
        }

        val consultationsCount = consultationPreviewFinishedRepository.getConsultationAnsweredCount(userId = userId)
        val offset = (pageNumber - 1) * MAX_PAGE_LIST_SIZE
        if (offset > consultationsCount) return null

        return ConsultationAnsweredPaginatedList(
            consultationAnsweredList = buildConsultationAnsweredPreviewList(userId = userId, offset = offset),
            maxPageNumber = ceil(consultationsCount.toDouble() / MAX_PAGE_LIST_SIZE.toDouble()).toInt(),
        ).also { cacheRepository.initConsultationAnsweredPage(userId = userId, pageNumber = pageNumber, content = it) }
    }

    private fun buildConsultationAnsweredPreviewList(userId: String, offset: Int): List<ConsultationPreviewFinished> {
        val thematiqueList = thematiqueRepository.getThematiqueList()
        return consultationPreviewFinishedRepository.getConsultationAnsweredList(userId = userId, offset = offset)
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

data class ConsultationAnsweredPaginatedList(
    val consultationAnsweredList: List<ConsultationPreviewFinished>,
    val maxPageNumber: Int,
)
