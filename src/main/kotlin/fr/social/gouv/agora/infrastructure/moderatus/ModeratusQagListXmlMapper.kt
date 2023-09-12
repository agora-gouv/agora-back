package fr.social.gouv.agora.infrastructure.moderatus

import fr.social.gouv.agora.domain.ModeratusQag
import fr.social.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import fr.social.gouv.agora.usecase.qag.ContentSanitizer
import org.springframework.stereotype.Component
import java.time.format.DateTimeFormatter

@Component
class ModeratusQagListXmlMapper(private val contentSanitizer: ContentSanitizer) {

    companion object {
        private const val TITLE_MAX_LENGTH = 200
        private const val DESCRIPTION_MAX_LENGTH = 400
        private const val USERNAME_MAX_LENGTH = 50
    }

    fun toXml(qags: List<ModeratusQag>): ModeratusQagListXml {
        val filteredQagToModerateList = qags.mapNotNull(::toXml)
        return ModeratusQagListXml(
            qagToModerateCount = filteredQagToModerateList.size,
            qagsToModerate = filteredQagToModerateList,
        )
    }

    private fun toXml(qag: ModeratusQag): ModeratusQagXml? {
        return if (isContentNotSaint(title = qag.title, description = qag.description, username = qag.username)) {
            null
        } else {
            ModeratusQagXml(
                qagId = qag.qagId,
                postDate = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(qag.date.toLocalDateTime()),
                userId = qag.userId,
                username = qag.username,
                title = qag.title,
                description = qag.description,
            )
        }
    }

    private fun isContentNotSaint(title: String, description: String, username: String) =
        !contentSanitizer.isContentSaint(title, TITLE_MAX_LENGTH)
                || !contentSanitizer.isContentSaint(description, DESCRIPTION_MAX_LENGTH)
                || !contentSanitizer.isContentSaint(username, USERNAME_MAX_LENGTH)

}