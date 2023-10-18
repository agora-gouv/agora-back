package fr.social.gouv.agora.usecase.qagSelection

import fr.social.gouv.agora.domain.QagSelectedForResponse
import org.springframework.stereotype.Service

@Service
class GetQagSelectedForResponseUseCase(
    private val filterGenerator: QagSelectForResponseFilterGenerator,
    private val mapper: QagSelectedForResponseMapper,
) {

    fun getQagSelectedForResponseList(userId: String): List<QagSelectedForResponse> {
        return TODO()
//        return getQagListUseCase
//            .getQagWithSupportAndThematique(filterGenerator.getFilter())
//            .sortedByDescending { qag -> qag.qagInfo.date }
//            .map { qag -> mapper.toQagSelectedForResponse(qag = qag, userId = userId) }
    }

}