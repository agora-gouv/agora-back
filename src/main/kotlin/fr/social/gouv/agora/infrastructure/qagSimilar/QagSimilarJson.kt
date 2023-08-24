package fr.social.gouv.agora.infrastructure.qagSimilar

import com.fasterxml.jackson.annotation.JsonProperty
import fr.social.gouv.agora.infrastructure.qagModerating.QagModeratingJson
import java.util.*

data class QagSimilarJson(
    @JsonProperty("similarQags")
    val similarQags: List<QagModeratingJson>,
)

data class QagHasSimilarJson (
    @JsonProperty("hasSimilar")
    val hasSimilar: Boolean,
)
