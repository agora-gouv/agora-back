package fr.gouv.agora.infrastructure.welcomePage.repository

import fr.gouv.agora.domain.News
import fr.gouv.agora.infrastructure.common.toHtml
import org.springframework.stereotype.Repository

@Repository
class NewsRepository(
    private val newsStrapiRepository: NewsStrapiRepository,
) {
    fun getNews(): List<News> {
        return newsStrapiRepository.getNews().map {
            News(
                it.attributes.message.toHtml(),
                it.attributes.callToAction,
                it.attributes.pageRouteMobile,
                it.attributes.pageRouteArgumentMobile,
                it.attributes.dateDeDebut,
            )
        }
    }
}
