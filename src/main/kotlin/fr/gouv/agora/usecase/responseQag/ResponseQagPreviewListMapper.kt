package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.IncomingResponsePreview
import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.domain.ResponseQagPreview
import fr.gouv.agora.domain.ResponseQagPreviewWithoutOrder
import fr.gouv.agora.domain.ResponseQagText
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDate
import fr.gouv.agora.usecase.qag.repository.QagInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.temporal.TemporalAdjusters

@Component
class ResponseQagPreviewListMapper {

    private val logger = LoggerFactory.getLogger(ResponseQagPreviewListMapper::class.java)

    companion object {
        private const val MAX_RESPONSE_TEXT_LENGTH = 200
    }

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
            responseText = sanitizeResponseText((responseQag as? ResponseQagText)?.responseText),
            username = qagInfo.username,
        )
    }

    private fun sanitizeResponseText(html: String?): String? {
        if (html == null) return null
        logger.info("sanitizeResponseText - texte brut avant nettoyage : $html")
        val plainText = Regex("<[^>]*>").replace(html, " ")
            .replace(Regex("\\s+"), " ")
            .trim()
        return if (plainText.length > MAX_RESPONSE_TEXT_LENGTH) {
            plainText.take(MAX_RESPONSE_TEXT_LENGTH) + "..."
        } else {
            plainText
        }
    }

    fun toIncomingResponsePreview(
        qagWithSupportCountAndOrder: QagWithSupportCountAndOrder,
        thematique: Thematique,
    ): IncomingResponsePreview {
        val qagDate = qagWithSupportCountAndOrder.qagWithSupportCount.date

        val dateLundiPrecedent = if (qagDate.toLocalDate().dayOfWeek == DayOfWeek.MONDAY) {
            qagDate.toLocalDate()
        } else {
            qagDate.toLocalDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY))
        }
        val dateLundiSuivant = qagDate.toLocalDate().with(TemporalAdjusters.next(DayOfWeek.MONDAY))

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
