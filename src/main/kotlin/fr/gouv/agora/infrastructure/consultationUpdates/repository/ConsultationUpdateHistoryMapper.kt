package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateHistory
import fr.gouv.agora.domain.ConsultationUpdateHistoryStatus
import fr.gouv.agora.domain.ConsultationUpdateHistoryType
import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.consultation.dto.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.consultationUpdates.dto.ConsultationUpdateHistoryWithDateDTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime
import java.util.Date

@Component
class ConsultationUpdateHistoryMapper(
    val clock: Clock,
) {
    fun toDomain(consultationStrapiDTO: StrapiAttributes<ConsultationStrapiDTO>): List<ConsultationUpdateHistory> {
        val contenuAvantReponse = consultationStrapiDTO.attributes.contenuAvantReponse.data
        val contenuApresReponse = consultationStrapiDTO.attributes.contenuApresReponseOuTerminee.data
        val autresContenusTriesParDate = consultationStrapiDTO.attributes.consultationContenuAutres.data
            .filter { it.attributes.datetimePublication.isAfter(LocalDateTime.now(clock)) }
            .sortedByDescending { it.attributes.datetimePublication }

        val dernierContenuId = if (autresContenusTriesParDate.isNotEmpty()) {
            autresContenusTriesParDate.last().id
        } else contenuApresReponse.id


        val historiqueAvantReponse = contenuAvantReponse.let {
            ConsultationUpdateHistory(
                ConsultationUpdateHistoryType.UPDATE,
                it.id,
                ConsultationUpdateHistoryStatus.DONE,
                it.attributes.historiqueTitre,
                it.attributes.datetimePublication.toDate(),
                it.attributes.historiqueCallToAction
            )
        }
        val historiqueApresReponse = contenuApresReponse.let {
            ConsultationUpdateHistory(
                ConsultationUpdateHistoryType.UPDATE,
                it.id,
                if (dernierContenuId == it.id) ConsultationUpdateHistoryStatus.DONE else ConsultationUpdateHistoryStatus.CURRENT,
                it.attributes.historiqueTitre,
                it.attributes.datetimePublication.toDate(),
                it.attributes.historiqueCallToAction
            )
        }
        val historiqueAutres = autresContenusTriesParDate
            .map {
                ConsultationUpdateHistory(
                    if (it.attributes.historiqueType == "contenu") ConsultationUpdateHistoryType.UPDATE else ConsultationUpdateHistoryType.RESULTS,
                    it.id,
                    if (dernierContenuId == it.id) ConsultationUpdateHistoryStatus.DONE else ConsultationUpdateHistoryStatus.CURRENT,
                    it.attributes.historiqueTitre,
                    it.attributes.datetimePublication.toDate(),
                    it.attributes.historiqueCallToAction
                )
            }

        val contenuAVenir = consultationStrapiDTO.attributes.consultationContenuAVenir?.data?.let {
            ConsultationUpdateHistory(
                ConsultationUpdateHistoryType.RESULTS,
                null,
                ConsultationUpdateHistoryStatus.INCOMING,
                it.attributes.titreHistorique,
                null,
                null,
            )
        }

        return listOfNotNull(
            contenuAVenir,
            *historiqueAutres.toTypedArray(),
            historiqueApresReponse,
            historiqueAvantReponse,
        )
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
            .reversed()
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
            "update" -> ConsultationUpdateHistoryType.UPDATE
            "results" -> ConsultationUpdateHistoryType.RESULTS
            else -> null
        }

        val status = if (dateNow.isBefore(historyItemDTO.updateDate?.toLocalDateTime() ?: LocalDateTime.MAX)) {
            ConsultationUpdateHistoryStatus.INCOMING
        } else {
            ConsultationUpdateHistoryStatus.DONE
        }

        return type?.let {
            ConsultationUpdateHistory(
                type = type,
                consultationUpdateId = historyItemDTO.consultationUpdateId?.toString(),
                status = status,
                title = historyItemDTO.title,
                updateDate = historyItemDTO.updateDate,
                actionText = historyItemDTO.actionText.takeIf { status == ConsultationUpdateHistoryStatus.DONE },
            )
        }
    }

    private fun List<ConsultationUpdateHistory>.replaceLastDoneStatusToCurrent(): List<ConsultationUpdateHistory> {
        val indexOfLastDoneStatus = this.indexOfLast { it.status == ConsultationUpdateHistoryStatus.DONE }
        return this
            .mapIndexed { index, historyItem ->
                if (index == indexOfLastDoneStatus) {
                    historyItem.copy(status = ConsultationUpdateHistoryStatus.CURRENT)
                } else historyItem
            }
    }

}
