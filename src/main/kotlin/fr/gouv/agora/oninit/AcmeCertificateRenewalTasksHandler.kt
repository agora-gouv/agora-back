package fr.gouv.agora.oninit

import fr.gouv.agora.usecase.acme.AcmeCertificateRenewalUseCase
import org.springframework.stereotype.Component

@Component
class AcmeCertificateRenewalTasksHandler(
    private val acmeCertificateRenewalUseCase: AcmeCertificateRenewalUseCase,
) : CustomCommandHandler {

    override fun handleTask(arguments: Map<String, String>?) {
        acmeCertificateRenewalUseCase.renewIfNeeded()
    }

}
