package fr.gouv.agora.usecase.welcomePage

import fr.gouv.agora.infrastructure.welcomePage.NewsJson
import fr.gouv.agora.infrastructure.welcomePage.repository.NewsDTO
import org.springframework.stereotype.Component

@Component
class NewsJsonMapper {
    fun toJson(newsDTO: NewsDTO): NewsJson {
        return NewsJson(
            newsDTO.description,
            newsDTO.callToActionText,
            newsDTO.routeName,
            newsDTO.routeArgument
        )
    }
}
