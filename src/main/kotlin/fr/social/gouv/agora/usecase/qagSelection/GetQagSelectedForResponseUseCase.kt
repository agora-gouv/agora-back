package fr.social.gouv.agora.usecase.qagSelection

import fr.social.gouv.agora.domain.QagSelectedForResponse
import fr.social.gouv.agora.usecase.qag.GetQagWithSupportAndThematiqueUseCase
import org.springframework.stereotype.Service

@Service
class GetQagSelectedForResponseUseCase(
    private val getQagListUseCase: GetQagWithSupportAndThematiqueUseCase,
    private val filterGenerator: QagSelectForResponseFilterGenerator,
    private val mapper: QagSelectedForResponseMapper,
) {

    fun getQagSelectedForResponseList(userId: String): List<QagSelectedForResponse> {
        return getQagListUseCase
            .getQagWithSupportAndThematique(filterGenerator.getFilter())
            .sortedByDescending { qag -> qag.qagInfo.date }
            .map { qag -> mapper.toQagSelectedForResponse(qag = qag, userId = userId) }
    }

}