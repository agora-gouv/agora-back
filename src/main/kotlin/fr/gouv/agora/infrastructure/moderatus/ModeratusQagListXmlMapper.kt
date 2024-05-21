package fr.gouv.agora.infrastructure.moderatus

import fr.gouv.agora.domain.ModeratusQag
import fr.gouv.agora.infrastructure.common.DateMapper
import org.springframework.stereotype.Component

@Component
class ModeratusQagListXmlMapper(private val dateMapper: DateMapper) {

    fun toXml(qags: List<ModeratusQag>): ModeratusQagListXml {
        return ModeratusQagListXml(
            qagToModerateCount = qags.size,
            qagsToModerate = qags.map(::toXml),
        )
    }

    private fun toXml(qag: ModeratusQag): ModeratusQagXml {
        return ModeratusQagXml(
            qagId = qag.qagId,
            postDate = dateMapper.toFormattedDate(qag.date),
            userId = qag.userId,
            username = qag.username,
            title = qag.title,
            description = qag.description,
        )
    }

}