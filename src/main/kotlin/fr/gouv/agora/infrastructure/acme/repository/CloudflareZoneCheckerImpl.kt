package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.usecase.acme.repository.CloudflareZoneChecker
import fr.gouv.agora.usecase.acme.repository.CloudflareZoneInfo
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class CloudflareZoneCheckerImpl(
    private val restTemplate: RestTemplate,
    private val acmeConfig: AcmeConfig,
) : CloudflareZoneChecker {

    private val logger = LoggerFactory.getLogger(CloudflareZoneCheckerImpl::class.java)

    override fun getZoneInfo(): CloudflareZoneInfo {
        val url = "${acmeConfig.cloudflareBaseUrl}/zones/${acmeConfig.cloudflareZoneId}"

        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${acmeConfig.cloudflareApiToken}")
            set("Content-Type", "application/json")
        }

        logger.info("Calling Cloudflare API to get zone info for zoneId=${acmeConfig.cloudflareZoneId}")

        @Suppress("UNCHECKED_CAST")
        val response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            HttpEntity<Void>(headers),
            Map::class.java,
        )

        val body = response.body
            ?: throw CloudflareZoneCheckException("Cloudflare API returned empty body for zone ${acmeConfig.cloudflareZoneId}")

        if (response.statusCode.is2xxSuccessful) {
            @Suppress("UNCHECKED_CAST")
            val result = body["result"] as? Map<String, Any>
                ?: throw CloudflareZoneCheckException("Cloudflare API response missing 'result' field")

            @Suppress("UNCHECKED_CAST")
            val plan = result["plan"] as? Map<String, Any>
            val planName = plan?.get("name") as? String ?: "unknown"

            return CloudflareZoneInfo(
                zoneId = acmeConfig.cloudflareZoneId,
                name = result["name"] as? String ?: "unknown",
                status = result["status"] as? String ?: "unknown",
                plan = planName,
            )
        } else {
            val errorBody = body.toString()
            logger.error("Cloudflare API error ${response.statusCode}: $errorBody")
            throw CloudflareZoneCheckException(
                "Cloudflare API returned ${response.statusCode} for zone ${acmeConfig.cloudflareZoneId}: $errorBody"
            )
        }
    }
}

class CloudflareZoneCheckException(message: String) : RuntimeException(message)
