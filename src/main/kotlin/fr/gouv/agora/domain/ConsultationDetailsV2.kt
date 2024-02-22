package fr.gouv.agora.domain

import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo

data class ConsultationDetailsV2(
    val consultation: ConsultationInfo,
    val thematique: Thematique,
    val update: ConsultationUpdateInfoV2,
    // TODO history
)

data class ConsultationDetailsV2WithInfo(
    val consultation: ConsultationInfo,
    val thematique: Thematique,
    val update: ConsultationUpdateInfoV2,
    // TODO history
    val participantCount: Int,
)
