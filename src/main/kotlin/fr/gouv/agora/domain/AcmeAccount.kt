package fr.gouv.agora.domain

data class AcmeAccount(
    val serverUrl: String,
    val accountUrl: String?,  // null si pas encore enregistré auprès de la CA
    val keyPem: String,       // PEM keypair du compte (en clair en mémoire, chiffré en base)
)
