package fr.gouv.agora.domain

import java.time.LocalDateTime

data class AcmeOrder(
    val domain: String,
    val orderUrl: String,
    val domainKeyPem: String,  // clé privée domaine (en clair en mémoire, chiffrée en base)
    val status: AcmeOrderStatus,
    val createdAt: LocalDateTime,
)
