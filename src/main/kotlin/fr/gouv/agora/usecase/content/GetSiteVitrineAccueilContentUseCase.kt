package fr.gouv.agora.usecase.content

import fr.gouv.agora.usecase.content.repository.ContentRepository
import fr.gouv.agora.domain.SiteVitrineAccueilContent
import org.springframework.stereotype.Service

@Service
class GetSiteVitrineAccueilContentUseCase(
    private val contentRepository: ContentRepository,
) {
    fun execute(): SiteVitrineAccueilContent {
        return contentRepository.getPageSiteVitrineAccueil()
    }
}
