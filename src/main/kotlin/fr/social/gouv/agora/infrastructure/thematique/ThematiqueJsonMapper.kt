package fr.social.gouv.agora.infrastructure.thematique

import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.utils.UnicodeStringDecoder
import org.springframework.stereotype.Component

@Component
class ThematiqueJsonMapper {
    fun toJson(domainList: List<Thematique>): ThematiquesJson {
        return ThematiquesJson(thematiques = domainList.map { domain ->
            ThematiqueJson(
                id = domain.id,
                label = domain.label,
                picto = UnicodeStringDecoder.decodeUnicode(domain.picto),
                color = domain.color,
            )
        })
    }
    fun thematiqueToJson(domain : Thematique) : ThematiqueJson {
        return ThematiqueJson(
            id = domain.id,
            label = domain.label,
            picto = UnicodeStringDecoder.decodeUnicode(domain.picto),
            color = domain.color,
        )
    }
}
