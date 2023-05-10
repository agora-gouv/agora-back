package fr.social.gouv.agora.infrastructure.thematique

import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.utils.UnicodeStringDecoder
import org.springframework.stereotype.Component

@Component
class ThematiqueJsonMapper {
    fun toJson(domainList: List<Thematique>): ThematiquesJson {
        return ThematiquesJson(thematiques = domainList.map(::toJson))
    }

    fun toJson(domain: Thematique): ThematiqueJson {
        return ThematiqueJson(
            id = domain.id,
            label = domain.label,
            picto = UnicodeStringDecoder.decodeUnicode(domain.picto),
            color = "", // TODO remove once removed from mobile app
        )
    }
}
