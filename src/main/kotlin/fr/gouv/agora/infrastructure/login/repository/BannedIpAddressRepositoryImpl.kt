package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.usecase.login.repository.BannedIpAddressHashRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class BannedIpAddressRepositoryImpl : BannedIpAddressHashRepository {

    companion object {
        private const val BANNED_IP_ADDRESS_HASH_SEPARATOR = ";"
    }

    override fun getBannedIpAddressesHash(): List<String> {
        return System.getenv("BANNED_IP_ADDRESSES_HASH")
            ?.split(BANNED_IP_ADDRESS_HASH_SEPARATOR)
            ?.filter { it.isNotBlank() }
            ?: emptyList()
    }

}