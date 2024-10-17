package fr.gouv.agora.usecase.content

import fr.gouv.agora.usecase.content.repository.ContentRepository
import fr.gouv.agora.usecase.content.repository.SiteVitrineQuestionAuGouvernementContent
import org.springframework.stereotype.Service

@Service
class GetSiteVitrineQuestionAuGouvernementContentUseCase(
    private val contentRepository: ContentRepository,
) {
    fun execute(): SiteVitrineQuestionAuGouvernementContent {
        return contentRepository.getPageSiteVitrineQuestionAuGouvernement()
    }
}
