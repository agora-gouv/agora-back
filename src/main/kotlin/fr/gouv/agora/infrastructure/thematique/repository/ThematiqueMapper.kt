package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import org.springframework.stereotype.Component

@Component
class ThematiqueMapper {

    fun toDomain(dto: ThematiqueDTO) = Thematique(
        id = dto.id.toString(),
        label = dto.label,
        picto = dto.picto,
    )

}
