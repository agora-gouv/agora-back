package fr.gouv.agora.usecase.acme.repository

import fr.gouv.agora.domain.AcmeCertificate

interface AcmeCertificateRepository {
    fun loadCertificate(domain: String): AcmeCertificate?  // null si aucun certificat en base
    fun saveCertificate(certificate: AcmeCertificate)
}
