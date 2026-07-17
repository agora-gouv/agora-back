package fr.gouv.agora.infrastructure.acme.repository

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.usecase.acme.repository.AcmeServerChecker
import fr.gouv.agora.usecase.acme.repository.AcmeServerDirectoryInfo
import org.slf4j.LoggerFactory
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class AcmeServerCheckerImpl(
    private val restTemplate: RestTemplate,
    private val acmeConfig: AcmeConfig,
) : AcmeServerChecker {

    private val logger = LoggerFactory.getLogger(AcmeServerCheckerImpl::class.java)

    override fun getDirectoryInfo(): AcmeServerDirectoryInfo {
        val directoryUrl = "${acmeConfig.serverUrl.trimEnd('/')}/directory"

        val headers = HttpHeaders().apply {
            set("Accept", "application/json")
        }

        logger.info("Calling ACME server directory endpoint: $directoryUrl")

        @Suppress("UNCHECKED_CAST")
        val response = restTemplate.exchange(
            directoryUrl,
            HttpMethod.GET,
            HttpEntity<Void>(headers),
            Map::class.java,
        )

        val body = response.body
            ?: throw AcmeServerCheckException("ACME server returned empty body from $directoryUrl")

        if (!response.statusCode.is2xxSuccessful) {
            throw AcmeServerCheckException(
                "ACME server returned ${response.statusCode} from $directoryUrl: ${body}"
            )
        }

        val newAccountUrl = body["newAccount"] as? String
            ?: throw AcmeServerCheckException("ACME directory response missing 'newAccount' field from $directoryUrl")

        val newOrderUrl = body["newOrder"] as? String
            ?: throw AcmeServerCheckException("ACME directory response missing 'newOrder' field from $directoryUrl")

        @Suppress("UNCHECKED_CAST")
        val meta = body["meta"] as? Map<String, Any>
        val termsOfServiceUrl = meta?.get("termsOfService") as? String

        return AcmeServerDirectoryInfo(
            serverUrl = acmeConfig.serverUrl,
            newAccountUrl = newAccountUrl,
            newOrderUrl = newOrderUrl,
            termsOfServiceUrl = termsOfServiceUrl,
        )
    }
}

class AcmeServerCheckException(message: String) : RuntimeException(message)
