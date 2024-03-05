package fr.gouv.agora.usecase.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewOngoing

interface ConsultationPreviewPageRepository {
    fun getConsultationPreviewOngoingList(): List<ConsultationPreviewOngoing>?
    fun insertConsultationPreviewOngoingList(ongoingList: List<ConsultationPreviewOngoing>)
    fun evictConsultationPreviewOngoingList()

    fun getConsultationPreviewFinishedList(): List<ConsultationPreviewFinished>?
    fun insertConsultationPreviewFinishedList(finishedList: List<ConsultationPreviewFinished>)
    fun evictConsultationPreviewFinishedList()

    fun getConsultationPreviewAnsweredList(userId: String): List<ConsultationPreviewFinished>?
    fun insertConsultationPreviewAnsweredList(userId: String, answeredList: List<ConsultationPreviewFinished>)
    fun evictConsultationPreviewAnsweredList(userId: String)

    fun clear()
}