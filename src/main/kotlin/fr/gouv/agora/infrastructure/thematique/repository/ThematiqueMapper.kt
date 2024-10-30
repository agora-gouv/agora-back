package fr.gouv.agora.infrastructure.thematique.repository

import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiData
import fr.gouv.agora.infrastructure.thematique.dto.ThematiqueStrapiDTO
import org.springframework.stereotype.Component

@Component
class ThematiqueMapper {
    fun toDomain(dto: ThematiqueStrapiDTO) = Thematique(
        id = dto.databaseId,
        label = dto.label,
        picto = dto.pictogramme,
    )

    fun toDomain(strapiDTO: StrapiDTO<ThematiqueStrapiDTO>): List<Thematique> {
        return strapiDTO.data.map { thematique ->
            toDomain(thematique.attributes)
        }
    }

    fun toDomain(strapiDTO: StrapiData<ThematiqueStrapiDTO>): Thematique {
        return toDomain(strapiDTO.data.attributes)
    }
}
