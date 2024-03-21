package fr.gouv.agora.usecase.login.repository

interface BannedIpAddressHashRepository {
    fun getBannedIpAddressesHash(): List<String>
}