package fr.gouv.agora.usecase.welcomePage

import fr.gouv.agora.domain.News
import fr.gouv.agora.infrastructure.welcomePage.NewsJson
import org.springframework.stereotype.Component

@Component
class NewsJsonMapper {
    fun toJson(newsDTO: News): NewsJson {
        return NewsJson(
            newsDTO.description,
            newsDTO.short_description,
            newsDTO.callToActionText,
            newsDTO.routeName,
            newsDTO.routeArgument
        )
    }
}
