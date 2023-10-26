package fr.gouv.agora.infrastructure.moderatus

import fr.gouv.agora.domain.ModeratusQagLockResult
import fr.gouv.agora.domain.QagLockResult
import org.springframework.stereotype.Component

@Component
class ModeratusQagLockResultXmlMapper {

    fun toXml(results: List<ModeratusQagLockResult>): ModeratusQagLockResultsXml {
        return ModeratusQagLockResultsXml(
            qagLockedResults = results.map(::toXml),
        )
    }

    fun toErrorXml(lockedQagIds: List<String>, errorMessage: String): ModeratusQagLockResultsXml {
        return ModeratusQagLockResultsXml(
            lockedQagIds.map { lockedQagId ->
                ModeratusQagLockResultXml(
                    qagId = lockedQagId,
                    result = "ERROR",
                    comment = errorMessage,
                )
            }
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