package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.domain.AcmeOrderStatus
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "acme_order")
data class AcmeOrderDAO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID = UUID.randomUUID(),

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    val domain: String,

    @Column(name = "order_url", columnDefinition = "VARCHAR(500)", nullable = false)
    val orderUrl: String,

    @Column(name = "domain_key_pem", columnDefinition = "TEXT", nullable = false)
    val domainKeyPem: String,  // clé privée domaine (chiffrée AES-256-GCM)

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(50)", nullable = false)
    val status: AcmeOrderStatus,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as AcmeOrderDAO
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
