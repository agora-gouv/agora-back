package fr.gouv.agora.usecase.acme.repository

import fr.gouv.agora.domain.AcmeOrder
import fr.gouv.agora.domain.AcmeOrderStatus

interface AcmeOrderRepository {
    fun loadOrder(domain: String): AcmeOrder?
    fun saveOrder(order: AcmeOrder)
    fun updateOrderStatus(domain: String, status: AcmeOrderStatus)
    fun deleteOrder(domain: String)
}
