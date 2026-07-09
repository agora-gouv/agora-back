package fr.gouv.agora.domain

enum class AcmeOrderStatus {
    CHALLENGE_PENDING,   // Challenge déclenché, polling Sectigo en attente
    ORDER_FINALIZING,    // Challenge validé, CSR envoyé, polling final en attente
}
