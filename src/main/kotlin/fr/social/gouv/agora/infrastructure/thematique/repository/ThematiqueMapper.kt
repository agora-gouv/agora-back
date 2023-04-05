package fr.social.gouv.agora.infrastructure.thematique.repository

import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import fr.social.gouv.agora.infrastructure.utils.Mapper
import org.springframework.stereotype.Component

@Component
class ThematiqueMapper : Mapper<Thematique, ThematiqueDTO> {

    override fun toDomain(dto: ThematiqueDTO) = Thematique(
        id = dto.id,
        label = dto.label,
        picto = dto.picto,
        color = dto.color,
    )

    override fun toDto(domain: Thematique) = ThematiqueDTO(
        id = domain.id,
        label = domain.label,
        picto = domain.picto,
        color = domain.color,
    )

}
