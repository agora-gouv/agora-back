package fr.gouv.agora.usecase.content

import fr.gouv.agora.usecase.content.repository.ContentRepository
import org.springframework.stereotype.Service

@Service
class GetSiteVitrinePolitiqueConfidentialiteContentUseCase(
    private val contentRepository: ContentRepository,
) {
    fun execute(): String {
        return contentRepository.getPageSiteVitrinePolitiqueConfidentialite()
    }
}
