package fr.social.gouv.agora.infrastructure.thematique.repository

import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import org.springframework.stereotype.Component

@Component
class ThematiqueMapper {

    fun toDomain(dto: ThematiqueDTO) = Thematique(
        id = dto.id.toString(),
        label = dto.label,
        picto = dto.picto,
    )

}
