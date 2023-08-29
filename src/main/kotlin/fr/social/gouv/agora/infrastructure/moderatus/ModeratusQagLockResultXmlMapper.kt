package fr.social.gouv.agora.infrastructure.moderatus

import fr.social.gouv.agora.domain.ModeratusQagLockResult
import fr.social.gouv.agora.domain.QagLockResult
import org.springframework.stereotype.Component

@Component
class ModeratusQagLockResultXmlMapper {

    fun toXml(results: List<ModeratusQagLockResult>): ModeratusQagLockResultsXml {
        return ModeratusQagLockResultsXml(
            qagLockedResults = results.map(::toXml),
        )
    }

    private fun toXml(result: ModeratusQagLockResult): ModeratusQagLockResultXml {
        return ModeratusQagLockResultXml(
            qagId = result.qagId,
            result = when (result.lockResult) {
                QagLockResult.SUCCESS -> "OK"
                QagLockResult.NOT_FOUND -> "NOTFOUND"
            },
            comment = null,
        )
    }

}