package fr.gouv.agora.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration

@Configuration
class AcmeConfig {
    @Value("\${ACME_ENABLED:false}")
    val enabled: Boolean = false

    @Value("\${ACME_SERVER_URL:}")
    val serverUrl: String = ""

    @Value("\${ACME_DOMAIN:}")
    val domain: String = ""

    @Value("\${ACME_EAB_KID:}")
    val eabKid: String = ""

    @Value("\${ACME_EAB_HMAC_KEY:}")
    val eabHmacKey: String = ""

    @Value("\${ACME_ENCRYPTION_KEY:}")
    val encryptionKey: String = ""

    @Value("\${CLOUDFLARE_ZONE_ID:}")
    val cloudflareZoneId: String = ""

    @Value("\${CLOUDFLARE_API_TOKEN:}")
    val cloudflareApiToken: String = ""

    @Value("\${ACME_SERVER_INTERACTION_ENABLED:false}")
    val acmeServerInteractionEnabled: Boolean = false

    @Value("\${ACME_CLOUDFLARE_INTERACTION_ENABLED:false}")
    val cloudflareInteractionEnabled: Boolean = false

    @Value("\${CLOUDFLARE_BASE_URL:https://api.cloudflare.com/client/v4}")
    val cloudflareBaseUrl: String = "https://api.cloudflare.com/client/v4"

    @Value("\${ACME_STUB_MODE:false}")
    val stubMode: Boolean = false

    @Value("\${server.port:8080}")
    val port: Int = 8080
}
