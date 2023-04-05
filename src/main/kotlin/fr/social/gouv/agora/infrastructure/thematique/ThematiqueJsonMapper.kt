package fr.social.gouv.agora.infrastructure.thematique

import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.utils.JsonMapper
import org.springframework.stereotype.Component

@Component
class ThematiqueJsonMapper : JsonMapper<List<Thematique>, ThematiquesJson> {
    override fun toJson(domain: List<Thematique>): ThematiquesJson {
        return ThematiquesJson(thematiques = domain.map { domainObject ->
            ThematiqueJson(
                id = domainObject.id,
                label = domainObject.label,
                picto = domainObject.picto,
                color = domainObject.color,
            )
        })
    }
}
