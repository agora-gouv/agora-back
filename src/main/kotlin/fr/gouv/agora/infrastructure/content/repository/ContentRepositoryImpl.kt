package fr.gouv.agora.infrastructure.content.repository

import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.usecase.content.repository.ContentRepository
import org.springframework.stereotype.Repository

@Repository
class ContentRepositoryImpl(
    val pagePoserMaQuestionStrapiRepository: PagePoserMaQuestionStrapiRepository,
    val pageQuestionsAuGouvernementStrapiRepository: PageQuestionsAuGouvernementStrapiRepository,
    val pageReponseAuxQuestionsAuGouvernementStrapiRepository: PageReponseAuxQuestionsAuGouvernementStrapiRepository,
) : ContentRepository {
    override fun getPagePoserMaQuestion(): String {
        return pagePoserMaQuestionStrapiRepository.getFirst().attributes.texteRegles.toHtml()
    }

    override fun getPageQuestionsAuGouvernement(): String {
        return pageQuestionsAuGouvernementStrapiRepository.getFirst().attributes.informationBottomsheet
    }

    override fun getPageReponseAuxQuestionsAuGouvernement(): String {
        return pageReponseAuxQuestionsAuGouvernementStrapiRepository.getFirst().attributes.informationReponseAVenirBottomsheet
    }
}
