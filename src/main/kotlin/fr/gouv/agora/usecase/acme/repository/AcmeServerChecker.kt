package fr.gouv.agora.usecase.acme.repository

interface AcmeServerChecker {
    fun getDirectoryInfo(): AcmeServerDirectoryInfo
}

data class AcmeServerDirectoryInfo(
    val serverUrl: String,
    val newAccountUrl: String,
    val newOrderUrl: String,
    val termsOfServiceUrl: String?,
)
