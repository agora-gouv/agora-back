package fr.social.gouv.agora.usecase.responseQag

import fr.social.gouv.agora.domain.IncomingResponsePreview
import fr.social.gouv.agora.domain.ResponseQag
import fr.social.gouv.agora.domain.ResponseQagPreview
import fr.social.gouv.agora.domain.Thematique
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import org.springframework.stereotype.Component

@Component
class ResponseQagPreviewListMapper {

    fun toResponseQagPreview(
        qag: QagInfo,
        thematique: Thematique,
        responseQag: ResponseQag,
    ): ResponseQagPreview {
        return ResponseQagPreview(
            qagId = responseQag.qagId,
            thematique = thematique,
            title = qag.title,
            author = responseQag.author,
            authorPortraitUrl = responseQag.authorPortraitUrl,
            responseDate = responseQag.responseDate,
        )
    }

    fun toResponseQagPreview(
        qag: QagInfoWithSupportCount,
        thematique: Thematique,
        responseQag: ResponseQag,
    ): ResponseQagPreview {
        return ResponseQagPreview(
            qagId = responseQag.qagId,
            thematique = thematique,
            title = qag.title,
            author = responseQag.author,
            authorPortraitUrl = responseQag.authorPortraitUrl,
            responseDate = responseQag.responseDate,
        )
    }

    fun toIncomingResponsePreview(qag: QagInfoWithSupportCount, thematique: Thematique): IncomingResponsePreview {
        return IncomingResponsePreview(
            id = qag.id,
            thematique = thematique,
            title = qag.title,
            supportCount = qag.supportCount,
        )
    }

}