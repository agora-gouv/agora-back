package fr.gouv.agora.infrastructure.moderatus

import fr.gouv.agora.domain.ModeratusQagModerateResult
import org.springframework.stereotype.Component

@Component
class ModeratusQagModerateResultPageXmlMapper {

    fun toXml(result: ModeratusQagModerateResult, errorMessage: String): ModeratusQagModerateResultPageXml {
        return ModeratusQagModerateResultPageXml(
            result = when (result) {
                ModeratusQagModerateResult.SUCCESS -> "OK"
                ModeratusQagModerateResult.NOT_FOUND -> "NOTFOUND"
                ModeratusQagModerateResult.FAILURE -> "ERROR"
            },
            error = result.takeIf { it == ModeratusQagModerateResult.FAILURE }?.let { errorMessage }
        )
    }

    fun toErrorXml(errorMessage: String): ModeratusQagModerateResultPageXml {
        return toXml(ModeratusQagModerateResult.FAILURE, errorMessage)
    }

}