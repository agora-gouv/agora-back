package fr.gouv.agora.domain

import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount

data class QagWithSupportCount(
    val qagInfo: QagInfoWithSupportCount,
    val thematique: Thematique,
)
