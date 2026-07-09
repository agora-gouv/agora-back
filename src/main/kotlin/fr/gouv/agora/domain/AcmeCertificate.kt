package fr.gouv.agora.domain

import java.time.LocalDateTime

data class AcmeCertificate(
    val domain: String,
    val certificatePem: String,  // PEM chain complet (cert + intermédiaires)
    val privateKeyPem: String,   // PEM clé privée (en clair en mémoire, chiffré en base)
    val expiresAt: LocalDateTime,
    val status: AcmeCertificateStatus = AcmeCertificateStatus.TO_DEPLOY,
    val deployedAt: LocalDateTime? = null,  // null si pas encore déployé sur Cloudflare
)
