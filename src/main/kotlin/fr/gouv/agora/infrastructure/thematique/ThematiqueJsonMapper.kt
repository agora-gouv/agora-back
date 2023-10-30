package fr.gouv.agora.infrastructure.thematique

import fr.gouv.agora.domain.Thematique
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
            picto = domain.picto,
        )
    }

    fun toNoIdJson(domain: Thematique): ThematiqueNoIdJson {
        return ThematiqueNoIdJson(
            label = domain.label,
            picto = domain.picto,
        )
    }

}
