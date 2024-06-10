package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.IncomingResponsePreview
import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagPreview
import fr.gouv.agora.domain.ResponseQagPreviewWithoutOrder
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDate
import fr.gouv.agora.usecase.qag.repository.QagInfo
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters

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
        val qagDate = qagWithSupportCountAndOrder.qagWithSupportCount.date
        val dateLundiPrecedent = qagDate.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
        var dateLundiSuivant = qagDate.toLocalDate().with(TemporalAdjusters.next(DayOfWeek.MONDAY))
        if(qagDate.toLocalDate().dayOfWeek == DayOfWeek.MONDAY) {
            dateLundiSuivant = qagDate.toLocalDate()
        }
        return IncomingResponsePreview(
            id = qagWithSupportCountAndOrder.qagWithSupportCount.id,
            thematique = thematique,
            title = qagWithSupportCountAndOrder.qagWithSupportCount.title,
            supportCount = qagWithSupportCountAndOrder.qagWithSupportCount.supportCount,
            dateLundiPrecedent = dateLundiPrecedent,
            dateLundiSuivant = dateLundiSuivant,
            order = qagWithSupportCountAndOrder.order,
        )
    }

}
