package fr.gouv.agora.infrastructure.acme.repository

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "acme_challenge")
data class AcmeChallengeDAO(
    @Id
    @Column(columnDefinition = "VARCHAR(255)", nullable = false)
    val token: String,

    @Column(name = "key_authorization", columnDefinition = "TEXT", nullable = false)
    val keyAuthorization: String,

    @Column(name = "created_at", nullable = false)
    val createdAt: LocalDateTime,
)
