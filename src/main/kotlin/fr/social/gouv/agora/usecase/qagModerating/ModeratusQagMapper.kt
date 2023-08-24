package fr.social.gouv.agora.usecase.qagModerating

import fr.social.gouv.agora.domain.ModeratusQag
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
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
        )
    }

}
