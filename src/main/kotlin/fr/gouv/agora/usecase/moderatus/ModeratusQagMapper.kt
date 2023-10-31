package fr.gouv.agora.usecase.moderatus

import fr.gouv.agora.domain.ModeratusQag
import fr.gouv.agora.usecase.qag.repository.QagInfo
import org.springframework.stereotype.Component

@Component
class ModeratusQagMapper {

    fun toModeratusQag(qagInfo: QagInfo): ModeratusQag {
        return ModeratusQag(
            qagId = qagInfo.id,
            title = qagInfo.title,
            description = qagInfo.description,
            date = qagInfo.date,
            username = qagInfo.username,
            userId = qagInfo.userId,
        )
    }

}
