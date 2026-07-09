package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.domain.AcmeCertificateStatus
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
@Table(name = "acme_certificate")
data class AcmeCertificateDAO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID = UUID.randomUUID(),

    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    val domain: String,

    @Column(columnDefinition = "TEXT", nullable = false)
    val certificate: String,

    @Column(name = "private_key", columnDefinition = "TEXT", nullable = false)
    val privateKey: String,

    @Column(name = "expires_at", nullable = false)
    val expiresAt: LocalDateTime,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "VARCHAR(50)", nullable = false)
    val status: AcmeCertificateStatus = AcmeCertificateStatus.TO_DEPLOY,

    @Column(name = "deployed_at", nullable = true)
    val deployedAt: LocalDateTime? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as AcmeCertificateDAO
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
