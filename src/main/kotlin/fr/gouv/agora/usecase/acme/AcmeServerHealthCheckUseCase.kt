package fr.gouv.agora.usecase.acme

import fr.gouv.agora.config.AcmeConfig
import fr.gouv.agora.usecase.acme.repository.AcmeServerChecker
import fr.gouv.agora.usecase.acme.repository.AcmeServerDirectoryInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

sealed class AcmeServerHealthCheckResult {
    data class DirectoryInfo(val info: AcmeServerDirectoryInfo) : AcmeServerHealthCheckResult()
    data class Disabled(val message: String) : AcmeServerHealthCheckResult()
}

@Service
class AcmeServerHealthCheckUseCase(
    private val acmeConfig: AcmeConfig,
    private val acmeServerChecker: AcmeServerChecker,
) {

    private val logger = LoggerFactory.getLogger(AcmeServerHealthCheckUseCase::class.java)

    fun getDirectoryInfo(): AcmeServerHealthCheckResult {
        if (!acmeConfig.enabled) {
            val message = "ACME is disabled (ACME_ENABLED=false)"
            logger.info(message)
            return AcmeServerHealthCheckResult.Disabled(message)
        }

        if (!acmeConfig.acmeServerInteractionEnabled) {
            val message = "ACME server interaction is disabled (ACME_SERVER_INTERACTION_ENABLED=false)"
            logger.info(message)
            return AcmeServerHealthCheckResult.Disabled(message)
        }

        logger.info("Fetching ACME server directory info from ${acmeConfig.serverUrl}")
        val directoryInfo = acmeServerChecker.getDirectoryInfo()
        logger.info("ACME server directory retrieved: newAccountUrl=${directoryInfo.newAccountUrl}, newOrderUrl=${directoryInfo.newOrderUrl}")
        return AcmeServerHealthCheckResult.DirectoryInfo(directoryInfo)
    }
}
