package fr.gouv.agora.usecase.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.usecase.acme.repository.CloudflareZoneChecker
import fr.gouv.agora.usecase.acme.repository.CloudflareZoneInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

sealed class CloudflareHealthCheckResult {
    data class ZoneInfo(val info: CloudflareZoneInfo) : CloudflareHealthCheckResult()
    data class Disabled(val message: String) : CloudflareHealthCheckResult()
}

@Service
class CloudflareHealthCheckUseCase(
    private val acmeConfig: AcmeConfig,
    private val cloudflareZoneChecker: CloudflareZoneChecker,
) {

    private val logger = LoggerFactory.getLogger(CloudflareHealthCheckUseCase::class.java)

    fun getZoneInfo(): CloudflareHealthCheckResult {
        if (!acmeConfig.cloudflareInteractionEnabled) {
            val message = "Cloudflare interaction is disabled (ACME_CLOUDFLARE_INTERACTION_ENABLED=false)"
            logger.info(message)
            return CloudflareHealthCheckResult.Disabled(message)
        }

        logger.info("Fetching Cloudflare zone info for zoneId=${acmeConfig.cloudflareZoneId}")
        val zoneInfo = cloudflareZoneChecker.getZoneInfo()
        logger.info("Cloudflare zone info retrieved: name=${zoneInfo.name}, status=${zoneInfo.status}, plan=${zoneInfo.plan}")
        return CloudflareHealthCheckResult.ZoneInfo(zoneInfo)
    }
}
