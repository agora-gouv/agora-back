package fr.gouv.agora.usecase.consultation.repository

import fr.gouv.agora.domain.ConsultationPreviewAnswered
import fr.gouv.agora.domain.ConsultationPreviewFinished
import fr.gouv.agora.domain.ConsultationPreviewOngoing

interface ConsultationPreviewPageRepository {
    fun getConsultationPreviewOngoingList(userId: String): List<ConsultationPreviewOngoing>?
    fun insertConsultationPreviewOngoingList(userId: String, ongoingList: List<ConsultationPreviewOngoing>)
    fun evictConsultationPreviewOngoingList(userId: String)

    fun getConsultationPreviewFinishedList(): List<ConsultationPreviewFinished>?
    fun insertConsultationPreviewFinishedList(finishedList: List<ConsultationPreviewFinished>)
    fun evictConsultationPreviewFinishedList()

    fun getConsultationPreviewAnsweredList(userId: String): List<ConsultationPreviewAnswered>?
    fun insertConsultationPreviewAnsweredList(userId: String, answeredList: List<ConsultationPreviewAnswered>)
    fun evictConsultationPreviewAnsweredList(userId: String)

    fun clear()
}