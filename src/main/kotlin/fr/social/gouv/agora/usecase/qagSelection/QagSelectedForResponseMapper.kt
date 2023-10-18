package fr.social.gouv.agora.usecase.qagSelection

import fr.social.gouv.agora.domain.QagSelectedForResponse
import fr.social.gouv.agora.domain.SupportQag
import org.springframework.stereotype.Component

@Component
class QagSelectedForResponseMapper {

//    fun toQagSelectedForResponse(qag: QagInfoWithSupportAndThematique, userId: String): QagSelectedForResponse {
//        return QagSelectedForResponse(
//            id = qag.qagInfo.id,
//            thematique = qag.thematique,
//            title = qag.qagInfo.title,
//            support = SupportQag(
//                supportCount = qag.supportQagInfoList.size,
//                isSupportedByUser = qag.supportQagInfoList.any { supportQagInfo -> supportQagInfo.userId == userId },
//            ),
//        )
//    }

}