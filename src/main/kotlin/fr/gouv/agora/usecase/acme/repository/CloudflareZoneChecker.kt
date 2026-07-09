package fr.gouv.agora.usecase.acme.repository

interface CloudflareZoneChecker {
    fun getZoneInfo(): CloudflareZoneInfo
}

data class CloudflareZoneInfo(
    val zoneId: String,
    val name: String,
    val status: String,
    val plan: String,
)
