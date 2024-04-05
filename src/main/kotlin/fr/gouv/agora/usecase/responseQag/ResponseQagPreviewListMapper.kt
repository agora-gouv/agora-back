package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.qag.repository.QagInfo
import org.springframework.stereotype.Component

@Component
class ResponseQagPreviewListMapper {

    fun toResponseQagPreview(
        qagWithResponseAndOrder: QagWithResponseAndOrder,
        thematique: Thematique,
    ): ResponseQagPreview {
        return ResponseQagPreview(
            qagId = qagWithResponseAndOrder.qagInfo.id,
            thematique = thematique,
            title = qagWithResponseAndOrder.qagInfo.title,
            author = qagWithResponseAndOrder.response.author,
            authorPortraitUrl = qagWithResponseAndOrder.response.authorPortraitUrl,
            responseDate = qagWithResponseAndOrder.response.responseDate,
            order = qagWithResponseAndOrder.order,
        )
    }

    fun toResponseQagPreviewWithoutOrder(
        qagInfo: QagInfo,
        responseQag: ResponseQag,
        thematique: Thematique,
    ): ResponseQagPreviewWithoutOrder {
        return ResponseQagPreviewWithoutOrder(
            qagId = qagInfo.id,
            thematique = thematique,
            title = qagInfo.title,
            author = responseQag.author,
            authorPortraitUrl = responseQag.authorPortraitUrl,
            responseDate = responseQag.responseDate,
        )
    }

    fun toIncomingResponsePreview(
        qagWithSupportCountAndOrder: QagWithSupportCountAndOrder,
        thematique: Thematique,
    ): IncomingResponsePreview {
        return IncomingResponsePreview(
            id = qagWithSupportCountAndOrder.qagWithSupportCount.id,
            thematique = thematique,
            title = qagWithSupportCountAndOrder.qagWithSupportCount.title,
            supportCount = qagWithSupportCountAndOrder.qagWithSupportCount.supportCount,
            order = qagWithSupportCountAndOrder.order,
        )
    }

}