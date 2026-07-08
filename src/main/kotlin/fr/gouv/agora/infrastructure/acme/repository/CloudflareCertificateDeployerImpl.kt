package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.usecase.acme.repository.CloudflareCertificateDeployer
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class CloudflareCertificateDeployerImpl(
    private val restTemplate: RestTemplate,
    private val acmeConfig: AcmeConfig,
) : CloudflareCertificateDeployer {

    private val logger = LoggerFactory.getLogger(CloudflareCertificateDeployerImpl::class.java)

    override fun deployCertificate(certificatePem: String, privateKeyPem: String) {
        val url = "https://api.cloudflare.com/client/v4/zones/${acmeConfig.cloudflareZoneId}/custom_certificates"

        val headers = HttpHeaders().apply {
            set("Authorization", "Bearer ${acmeConfig.cloudflareApiToken}")
            set("Content-Type", "application/json")
        }

        val body = mapOf(
            "certificate" to certificatePem,
            "private_key" to privateKeyPem,
            "bundle_method" to "ubiquitous",
        )

        logger.info("Deploying certificate to Cloudflare for zone ${acmeConfig.cloudflareZoneId}")

        val response = restTemplate.exchange(
            url,
            HttpMethod.PATCH,
            HttpEntity(body, headers),
            Map::class.java,
        )

        if (response.statusCode.is2xxSuccessful) {
            logger.info("Certificate successfully deployed to Cloudflare")
        } else {
            val errorBody = response.body?.toString() ?: "empty body"
            logger.error("Cloudflare API error ${response.statusCode}: $errorBody")
            throw CloudflareDeploymentException(
                "Cloudflare API returned ${response.statusCode} for zone ${acmeConfig.cloudflareZoneId}: $errorBody"
            )
        }
    }
}

class CloudflareDeploymentException(message: String) : RuntimeException(message)
