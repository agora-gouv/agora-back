package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateHistory
import fr.gouv.agora.domain.ConsultationUpdateHistoryStatus
import fr.gouv.agora.domain.ConsultationUpdateHistoryType
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateHistoryWithDateDTO
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationUpdateHistoryMapper(
    val clock: Clock,
) {

    companion object {
        private const val TYPE_UPDATE = "update"
        private const val TYPE_RESULTS = "results"
    }

    fun toDomain(dtoList: List<ConsultationUpdateHistoryWithDateDTO>): List<ConsultationUpdateHistory> {
        val dateNow = LocalDateTime.now()
        return dtoList.fold<ConsultationUpdateHistoryWithDateDTO, List<ConsultationUpdateHistory>>(
            initial = emptyList(),
            operation = { buildingHistoryList, historyItemDTO ->
                return if (!hasAlreadySameStepOrHasReachedMaxIncomingItems(buildingHistoryList, historyItemDTO)) {
                    val newHistoryItem = buildHistoryItem(
                        dateNow = dateNow,
                        dtoList = dtoList,
                        buildingHistoryList = buildingHistoryList,
                        historyItemDTO = historyItemDTO,
                    )
                    if (newHistoryItem != null) {
                        buildingHistoryList + newHistoryItem
                    } else buildingHistoryList
                } else buildingHistoryList
            }
        ).reversed()
    }

    private fun hasAlreadySameStepOrHasReachedMaxIncomingItems(
        buildingHistoryList: List<ConsultationUpdateHistory>,
        historyItemDTO: ConsultationUpdateHistoryWithDateDTO,
    ) = buildingHistoryList.any { historyItem ->
        historyItem.status == ConsultationUpdateHistoryStatus.INCOMING
                || historyItem.stepNumber == historyItemDTO.stepNumber
    }

    private fun buildHistoryItem(
        dateNow: LocalDateTime,
        dtoList: List<ConsultationUpdateHistoryWithDateDTO>,
        buildingHistoryList: List<ConsultationUpdateHistory>,
        historyItemDTO: ConsultationUpdateHistoryWithDateDTO,
    ): ConsultationUpdateHistory? {
        val type = when (historyItemDTO.type) {
            TYPE_UPDATE -> ConsultationUpdateHistoryType.UPDATE
            TYPE_RESULTS -> ConsultationUpdateHistoryType.RESULTS
            else -> null
        }
        // TODO other status cases
        return type?.let {
            ConsultationUpdateHistory(
                stepNumber = historyItemDTO.stepNumber,
                type = type,
                consultationUpdateId = historyItemDTO.consultationUpdateId?.toString(),
                status = ConsultationUpdateHistoryStatus.CURRENT,
                title = historyItemDTO.title,
                updateDate = historyItemDTO.updateDate,
                actionText = historyItemDTO.actionText,
            )
        }
    }

}
