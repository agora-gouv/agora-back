package fr.gouv.agora.usecase.content

import fr.gouv.agora.usecase.content.repository.ContentRepository
import org.springframework.stereotype.Service

@Service
class GetInformationsReponsesAVenirUseCase(
    private val contentRepository: ContentRepository,
) {
    // todo refacto : ajouter la notion de page dans les controllers/usecase
    fun execute(): String {
        return contentRepository.getPageReponseAuxQuestionsAuGouvernement()
    }
}
