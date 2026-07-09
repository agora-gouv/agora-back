package fr.gouv.agora.infrastructure.admin

import fr.gouv.agora.domain.QagStatus
import io.swagger.v3.oas.annotations.media.Schema

data class UpdateQagStatusJson(
    @Schema(
        description = "Nouveau statut de la question",
        implementation = QagStatus::class,
        required = true,
    )
    val status: QagStatus,
)
