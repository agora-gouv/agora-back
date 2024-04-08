package fr.gouv.agora.usecase.qag.repository

interface LowPriorityQagRepository {
    fun getLowPriorityQagIds(qagIds: List<String>): List<String>
}