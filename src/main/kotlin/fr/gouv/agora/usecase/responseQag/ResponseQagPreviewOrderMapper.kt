package fr.gouv.agora.usecase.responseQag

import fr.gouv.agora.domain.ResponseQag
import fr.gouv.agora.usecase.qag.repository.QagInfo
import fr.gouv.agora.usecase.qag.repository.QagInfoWithSupportCount
import org.springframework.stereotype.Component
import java.util.*

@Component
class ResponseQagPreviewOrderMapper {

    fun buildOrderResult(
        lowPriorityQagIds: List<String>,
        incomingResponses: List<QagInfoWithSupportCount>,
        responses: List<Pair<QagInfoWithSupportCount, ResponseQag>>,
    ): ResponseQagPreviewOrderResult {
        val orderDataList = (incomingResponses.map {
            buildOrderData(
                qagWithSupportCount = it,
                lowPriorityQagIds = lowPriorityQagIds,
            )
        } + responses.map {
            buildOrderData(
                qagAndResponse = it,
                lowPriorityQagIds = lowPriorityQagIds,
            )
        }).sortedWith(compareBy<BuildOrderData<*>> { it.isLowPriority }.thenByDescending { it.date })

        return ResponseQagPreviewOrderResult(
            incomingResponses = orderDataList.mapIndexedNotNull { index, orderData ->
                if (orderData.data is QagInfoWithSupportCount) {
                    QagWithSupportCountAndOrder(
                        qagWithSupportCount = orderData.data,
                        order = index,
                    )
                } else null
            },
            responses = orderDataList.mapIndexedNotNull { index, orderData ->
                if (orderData.data is Pair<*, *>) {
                    QagWithResponseAndOrder(
                        qagInfo = orderData.data.first as QagInfoWithSupportCount,
                        response = orderData.data.second as ResponseQag,
                        order = index,
                    )
                } else null
            },
        )
    }

    private fun buildOrderData(
        qagWithSupportCount: QagInfoWithSupportCount,
        lowPriorityQagIds: List<String>,
    ): BuildOrderData<*> {
        return BuildOrderData(
            date = qagWithSupportCount.date,
            isLowPriority = lowPriorityQagIds.contains(qagWithSupportCount.id),
            data = qagWithSupportCount,
        )
    }

    private fun buildOrderData(
        qagAndResponse: Pair<QagInfoWithSupportCount, ResponseQag>,
        lowPriorityQagIds: List<String>,
    ): BuildOrderData<*> {
        return BuildOrderData(
            date = qagAndResponse.second.responseDate,
            isLowPriority = lowPriorityQagIds.contains(qagAndResponse.first.id),
            data = qagAndResponse,
        )
    }

}

data class BuildOrderData<T>(
    val date: Date,
    val isLowPriority: Boolean,
    val data: T,
)

data class ResponseQagPreviewOrderResult(
    val incomingResponses: List<QagWithSupportCountAndOrder>,
    val responses: List<QagWithResponseAndOrder>,
)
