package fr.social.gouv.agora.infrastructure.moderatus

import fr.social.gouv.agora.domain.ModeratusQag
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class ModeratusQagListXmlMapper {

    fun toXml(qags: List<ModeratusQag>): ModeratusQagListXml {
        return ModeratusQagListXml(
            qagToModerateCount = qags.size,
            qagsToModerate = qags.map(::toXml)
        )
    }

    private fun toXml(qag: ModeratusQag): ModeratusQagXml {
        return ModeratusQagXml(
            qagId = qag.qagId,
            postDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(qag.date.toLocalDateTime()),
            userId = qag.userId,
            username = wrapWithCDATA(qag.username),
            title = wrapWithCDATA(qag.title),
            description = wrapWithCDATA(qag.description),
        )
    }

    private fun wrapWithCDATA(text: String) = "<![CDATA[$text]]>"

}