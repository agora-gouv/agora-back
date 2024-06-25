package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.thematique.dto.StrapiThematiqueDTO
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueDTO
import org.springframework.stereotype.Component

@Component
class ThematiqueMapper {

    fun toDomain(dto: ThematiqueDTO) = Thematique(
        id = dto.id.toString(),
        label = dto.label,
        picto = dto.picto,
    )

    fun toDomain(strapiDTO: StrapiDTO<StrapiThematiqueDTO>): List<Thematique> {
        return strapiDTO.data.map { thematique ->
            Thematique(
                thematique.attributes.databaseId,
                thematique.attributes.label,
                thematique.attributes.pictogramme
            )
        }
    }
}
