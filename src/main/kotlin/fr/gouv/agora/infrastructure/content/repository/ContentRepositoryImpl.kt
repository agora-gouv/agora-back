package fr.gouv.agora.infrastructure.content.repository

import fr.gouv.agora.usecase.content.repository.ContentRepository
import org.springframework.stereotype.Repository

@Repository
class ContentRepositoryImpl(
    val pagePoserMaQuestionStrapiRepository: PagePoserMaQuestionStrapiRepository,
    val pageQuestionsAuGouvernementStrapiRepository: PageQuestionsAuGouvernementStrapiRepository,
    val pageReponseAuxQuestionsAuGouvernementStrapiRepository: PageReponseAuxQuestionsAuGouvernementStrapiRepository,
) : ContentRepository {
    override fun getPagePoserMaQuestion(): String {
        return pagePoserMaQuestionStrapiRepository.getFirst().attributes.texteRegles
    }

    override fun getPageQuestionsAuGouvernement(): String {
        return pageQuestionsAuGouvernementStrapiRepository.getFirst().attributes.informationBottosheet
    }

    override fun getPageReponseAuxQuestionsAuGouvernement(): String {
        return pageReponseAuxQuestionsAuGouvernementStrapiRepository.getFirst().attributes.informationReponseAVenirBottomsheet
    }
}
