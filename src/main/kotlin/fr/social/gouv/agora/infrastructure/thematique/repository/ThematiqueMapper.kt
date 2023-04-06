package fr.social.gouv.agora.infrastructure.thematique.repository

import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import fr.social.gouv.agora.infrastructure.utils.Mapper
import org.springframework.stereotype.Component
import java.util.*

@Component
class ThematiqueMapper : Mapper<Thematique, ThematiqueDTO> {

    override fun toDomain(dto: ThematiqueDTO) = Thematique(
        id = dto.id.toString(),
        label = dto.label,
        picto = dto.picto,
        color = dto.color,
    )

    override fun toDto(domain: Thematique) = ThematiqueDTO(
        id = UUID.fromString(domain.id),
        label = domain.label,
        picto = domain.picto,
        color = domain.color,
    )

}
