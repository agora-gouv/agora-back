package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateHistory
import fr.gouv.agora.domain.ConsultationUpdateHistoryStatus
import fr.gouv.agora.domain.ConsultationUpdateHistoryType
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateHistoryWithDateDTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.util.*

@Component
class ConsultationUpdateHistoryMapper(
    val clock: Clock,
) {

    companion object {
        private const val TYPE_UPDATE = "update"
        private const val TYPE_RESULTS = "results"
    }

    fun toDomain(dtoList: List<ConsultationUpdateHistoryWithDateDTO>): List<ConsultationUpdateHistory> {
        val dateNow = LocalDateTime.now(clock)
        return dtoList
            .filterOneItemPerStep()
            .sortedBy { it.stepNumber }
            .fold<ConsultationUpdateHistoryWithDateDTO, List<ConsultationUpdateHistory>>(
                initial = emptyList(),
                operation = { buildingHistoryList, historyItemDTO ->
                    if (!hasReachedMaxIncomingItems(buildingHistoryList)) {
                        val newHistoryItem = buildHistoryItem(dateNow = dateNow, historyItemDTO = historyItemDTO)
                        if (newHistoryItem != null) {
                            buildingHistoryList + newHistoryItem
                        } else buildingHistoryList
                    } else buildingHistoryList
                }
            )
            .replaceLastDoneStatusToCurrent()
    }

    private fun List<ConsultationUpdateHistoryWithDateDTO>.filterOneItemPerStep(): List<ConsultationUpdateHistoryWithDateDTO> {
        return this
            .groupBy { it.stepNumber }
            .mapNotNull { (_, sameStepDTOs) -> sameStepDTOs.maxByOrNull { it.updateDate ?: Date(0) } }
    }

    private fun hasReachedMaxIncomingItems(
        buildingHistoryList: List<ConsultationUpdateHistory>
    ) = buildingHistoryList.any { historyItem ->
        historyItem.status == ConsultationUpdateHistoryStatus.INCOMING
    }

    private fun buildHistoryItem(
        dateNow: LocalDateTime,
        historyItemDTO: ConsultationUpdateHistoryWithDateDTO,
    ): ConsultationUpdateHistory? {
        val type = when (historyItemDTO.type) {
            TYPE_UPDATE -> ConsultationUpdateHistoryType.UPDATE
            TYPE_RESULTS -> ConsultationUpdateHistoryType.RESULTS
            else -> null
        }

        val status = if (dateNow.isBefore(historyItemDTO.updateDate?.toLocalDateTime() ?: LocalDateTime.MAX)) {
            ConsultationUpdateHistoryStatus.INCOMING
        } else {
            ConsultationUpdateHistoryStatus.DONE
        }

        return type?.let {
            ConsultationUpdateHistory(
                stepNumber = historyItemDTO.stepNumber,
                type = type,
                consultationUpdateId = historyItemDTO.consultationUpdateId?.toString(),
                status = status,
                title = historyItemDTO.title,
                updateDate = historyItemDTO.updateDate,
                actionText = historyItemDTO.actionText,
            )
        }
    }

    private fun List<ConsultationUpdateHistory>.replaceLastDoneStatusToCurrent(): List<ConsultationUpdateHistory> {
        val indexOfFirstDoneStatus = this.indexOfLast { it.status == ConsultationUpdateHistoryStatus.DONE }
        return this
            .mapIndexed { index, historyItem ->
                if (index == indexOfFirstDoneStatus) {
                    historyItem.copy(status = ConsultationUpdateHistoryStatus.CURRENT)
                } else historyItem
            }
    }

}
