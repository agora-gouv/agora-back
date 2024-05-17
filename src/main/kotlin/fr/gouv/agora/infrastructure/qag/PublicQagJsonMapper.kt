package fr.gouv.agora.infrastructure.qag

import fr.gouv.agora.domain.QagDetails
import fr.gouv.agora.infrastructure.common.DateMapper
import fr.gouv.agora.infrastructure.thematique.ThematiqueNoIdJson
import org.springframework.stereotype.Component

@Component
class PublicQagJsonMapper(private val dateMapper: DateMapper) {

    fun toJson(qagDetails: QagDetails): PublicQagJson {
        return PublicQagJson(
            id = qagDetails.id,
            thematique = ThematiqueNoIdJson(
                label = qagDetails.thematique.label,
                picto = qagDetails.thematique.picto,
            ),
            title = qagDetails.title,
            description = qagDetails.description,
            date = dateMapper.toFormattedDate(qagDetails.date),
            username = qagDetails.username,
            supportCount = qagDetails.supportCount,
        )
    }

}