package fr.gouv.agora.infrastructure.welcomePage.repository

import fr.gouv.agora.domain.AgoraFeature.*
import fr.gouv.agora.domain.News
import fr.gouv.agora.infrastructure.common.toHtml
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import org.springframework.stereotype.Repository

@Repository
class NewsRepository(
    private val newsDatabaseRepository: NewsDatabaseRepository,
    private val newsStrapiRepository: NewsStrapiRepository,
    private val featureFlagsRepository: FeatureFlagsRepository
) {
    fun getNews(): List<News> {
        return if (featureFlagsRepository.isFeatureEnabled(StrapiNews)) {
            newsStrapiRepository.getNews().map {
                News(
                    it.attributes.message.toHtml(),
                    it.attributes.callToAction,
                    it.attributes.pageRouteMobile,
                    it.attributes.pageRouteArgumentMobile,
                    it.attributes.dateDeDebut,
                )
            }
        } else {
            newsDatabaseRepository.getNewsList().map {
                News(
                    it.description,
                    it.callToActionText,
                    it.routeName,
                    it.routeArgument,
                    it.beginDate.atStartOfDay()
                )
            }
        }
    }
}
