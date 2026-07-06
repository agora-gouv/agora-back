package fr.gouv.agora.infrastructure.acme.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.time.LocalDateTime
import java.util.UUID

@Entity
@Table(name = "acme_account")
data class AcmeAccountDAO(
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(columnDefinition = "UUID DEFAULT gen_random_uuid()")
    val id: UUID = UUID.randomUUID(),

    @Column(name = "server_url", columnDefinition = "VARCHAR(500)", nullable = false)
    val serverUrl: String,

    @Column(name = "account_url", columnDefinition = "VARCHAR(500)", nullable = true)
    val accountUrl: String? = null,

    @Column(name = "key_pem", columnDefinition = "TEXT", nullable = false)
    val keyPem: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as AcmeAccountDAO
        return id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}
