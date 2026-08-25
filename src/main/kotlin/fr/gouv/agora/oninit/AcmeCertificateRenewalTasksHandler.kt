package fr.gouv.agora.oninit

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.usecase.acme.AcmeCertificateRenewalUseCase
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class AcmeCertificateRenewalTasksHandler(
    private val acmeConfig: AcmeConfig,
    private val acmeCertificateRenewalUseCase: AcmeCertificateRenewalUseCase,
) : CustomCommandHandler {

    private val logger = LoggerFactory.getLogger(AcmeCertificateRenewalTasksHandler::class.java)

    override fun handleTask(arguments: Map<String, String>?) {
        if (!acmeConfig.cronEnabled) {
            logger.info("ACME cron renewal is disabled (ACME_CRON_ENABLED=false). Skipping.")
            return
        }
        acmeCertificateRenewalUseCase.renewIfNeeded()
    }

}
