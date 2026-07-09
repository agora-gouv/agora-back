package fr.gouv.agora.usecase.acme.repository

interface CloudflareCertificateDeployer {
    /** Lance le PATCH Cloudflare. Lève une exception si l'API répond != 2xx */
    fun deployCertificate(certificatePem: String, privateKeyPem: String)
}
