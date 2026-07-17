package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.domain.AcmeOrder
import fr.gouv.agora.domain.AcmeOrderStatus
import fr.gouv.agora.usecase.acme.repository.AcmeOrderRepository
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class AcmeOrderRepositoryImpl(
    private val jpaRepository: AcmeOrderJpaRepository,
    private val cryptoHelper: AcmeCryptoHelper,
) : AcmeOrderRepository {

    override fun loadOrder(domain: String): AcmeOrder? {
        val dao = jpaRepository.findFirstByDomainOrderByCreatedAtDesc(domain) ?: return null
        return AcmeOrder(
            domain = dao.domain,
            orderUrl = dao.orderUrl,
            domainKeyPem = cryptoHelper.decrypt(dao.domainKeyPem),
            status = dao.status,
            createdAt = dao.createdAt,
        )
    }

    override fun saveOrder(order: AcmeOrder) {
        val dao = AcmeOrderDAO(
            domain = order.domain,
            orderUrl = order.orderUrl,
            domainKeyPem = cryptoHelper.encrypt(order.domainKeyPem),
            status = order.status,
            createdAt = LocalDateTime.now(),
        )
        jpaRepository.save(dao)
    }

    override fun updateOrderStatus(domain: String, status: AcmeOrderStatus) {
        jpaRepository.updateStatusForLatestByDomain(domain, status)
    }

    override fun deleteOrder(domain: String) {
        jpaRepository.deleteByDomain(domain)
    }
}
