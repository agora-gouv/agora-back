package fr.social.gouv.agora.usecase.responseQag

import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import org.springframework.stereotype.Component

@Component
class ResponseQagPreviewMapper {

    fun toResponseQagPreview(
        responseQag: ResponseQag,
        qagInfo: QagInfo,
        thematique: Thematique,
    ): ResponseQagPreview {
        return ResponseQagPreview(
            qagId = responseQag.qagId,
            thematique = thematique,
            title = qagInfo.title,
            author = responseQag.author,
            authorPortraitUrl = responseQag.authorPortraitUrl,
            responseDate = responseQag.responseDate,
        )
    }
}