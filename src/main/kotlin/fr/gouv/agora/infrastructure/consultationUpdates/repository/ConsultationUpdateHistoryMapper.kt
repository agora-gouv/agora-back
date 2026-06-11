package fr.gouv.agora.infrastructure.consultationUpdates.repository

import fr.gouv.agora.domain.ConsultationUpdateHistory
import fr.gouv.agora.domain.ConsultationUpdateHistoryStatus
import fr.gouv.agora.domain.ConsultationUpdateHistoryType
import fr.gouv.agora.infrastructure.consultation.dto.strapi.ConsultationStrapiDTO
import fr.gouv.agora.infrastructure.utils.DateUtils.toDate
import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
class ConsultationUpdateHistoryMapper(
    val clock: Clock,
) {
    fun toDomain(consultationStrapiDTO: ConsultationStrapiDTO): List<ConsultationUpdateHistory> {
        val contenuAvantReponse = consultationStrapiDTO.contenuAvantReponse
        val contenuApresReponse = consultationStrapiDTO.contenuApresReponseOuTerminee
        val contenuAnalyseDesReponses = consultationStrapiDTO.consultationContenuAnalyseDesReponses
        val contenuReponseCommanditaire = consultationStrapiDTO.consultationContenuReponseDuCommanditaire
        val autresContenusTriesParDate = consultationStrapiDTO.consultationContenuAutres
            .filter { it.datetimePublication.isBefore(LocalDateTime.now(clock)) }
            .sortedByDescending { it.datetimePublication }

        val dernierContenuId = if (autresContenusTriesParDate.isNotEmpty()) {
            autresContenusTriesParDate.first().documentId
        } else if (contenuReponseCommanditaire != null) {
            contenuReponseCommanditaire.documentId
        } else if (contenuAnalyseDesReponses != null) {
            contenuAnalyseDesReponses.documentId
        } else contenuApresReponse.documentId

        val historiqueAvantReponse = contenuAvantReponse.let {
            ConsultationUpdateHistory(
                ConsultationUpdateHistoryType.UPDATE,
                it.documentId,
                ConsultationUpdateHistoryStatus.DONE,
                it.historiqueTitre,
                it.slug,
                consultationStrapiDTO.dateDeDebut.toDate(),
                it.historiqueCallToAction
            )
        }
        val historiqueApresReponse = contenuApresReponse.let {
            ConsultationUpdateHistory(
                ConsultationUpdateHistoryType.RESULTS,
                it.documentId,
                if (dernierContenuId == it.documentId) ConsultationUpdateHistoryStatus.CURRENT else ConsultationUpdateHistoryStatus.DONE,
                it.historiqueTitre,
                it.slug,
                consultationStrapiDTO.dateDeFin.toDate(),
                it.historiqueCallToAction
            )
        }
        val historiqueAnalyseDesReponses = contenuAnalyseDesReponses?.let {
            ConsultationUpdateHistory(
                ConsultationUpdateHistoryType.UPDATE,
                it.documentId,
                if (dernierContenuId == it.documentId) ConsultationUpdateHistoryStatus.CURRENT else ConsultationUpdateHistoryStatus.DONE,
                it.historiqueTitre,
                it.slug,
                it.datetimePublication.toDate(),
                it.historiqueCallToAction
            )
        }
        val historiqueReponseCommanditaire = contenuReponseCommanditaire?.let {
            ConsultationUpdateHistory(
                ConsultationUpdateHistoryType.UPDATE,
                it.documentId,
                if (dernierContenuId == it.documentId) ConsultationUpdateHistoryStatus.CURRENT else ConsultationUpdateHistoryStatus.DONE,
                it.historiqueTitre,
                it.slug,
                it.datetimePublication.toDate(),
                it.historiqueCallToAction
            )
        }
        val historiqueAutres = autresContenusTriesParDate
            .map {
                ConsultationUpdateHistory(
                    ConsultationUpdateHistoryType.UPDATE,
                    it.documentId,
                    if (dernierContenuId == it.documentId) ConsultationUpdateHistoryStatus.CURRENT else ConsultationUpdateHistoryStatus.DONE,
                    it.historiqueTitre,
                    it.slug,
                    it.datetimePublication.toDate(),
                    it.historiqueCallToAction
                )
            }
        val historiqueContenuAVenir = consultationStrapiDTO.consultationContenuAVenir?.let {
            ConsultationUpdateHistory(
                ConsultationUpdateHistoryType.UPDATE,
                null,
                ConsultationUpdateHistoryStatus.INCOMING,
                it.titreHistorique,
                null,
                null,
                null,
            )
        }

        return (listOfNotNull(
            historiqueContenuAVenir,
            historiqueReponseCommanditaire,
            historiqueAnalyseDesReponses,
            historiqueApresReponse,
            historiqueAvantReponse,
        ) + historiqueAutres)
            .filter { it.updateDate?.toLocalDateTime()?.isBefore(LocalDateTime.now(clock)) ?: true }
    }
}
