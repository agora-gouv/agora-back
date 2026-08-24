package fr.gouv.agora.infrastructure.acme.stub

import org.bouncycastle.asn1.x500.X500Name
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.math.BigInteger
import java.security.KeyPairGenerator
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit
import java.util.Base64
import java.util.Date

data class GeneratedCertificate(
    val certPem: String,
    val privateKeyPem: String,
    val expiresAt: LocalDateTime,
)

@Component
@ConditionalOnProperty(name = ["ACME_STUB_MODE"], havingValue = "true")
class AcmeStubCertificateGenerator {

    fun generate(domain: String): GeneratedCertificate {
        val keyPair = KeyPairGenerator.getInstance("RSA").apply { initialize(2048) }.generateKeyPair()

        val notBefore = Date.from(Instant.now())
        val notAfter = Date.from(Instant.now().plus(90, ChronoUnit.DAYS))

        val subject = X500Name("CN=$domain")
        val certHolder = JcaX509v3CertificateBuilder(
            subject, BigInteger.valueOf(System.currentTimeMillis()),
            notBefore, notAfter, subject, keyPair.public
        ).build(JcaContentSignerBuilder("SHA256withRSA").build(keyPair.private))

        val cert = JcaX509CertificateConverter().getCertificate(certHolder)
        val encoder = Base64.getMimeEncoder(64, "\n".toByteArray())

        val certPem = "-----BEGIN CERTIFICATE-----\n${encoder.encodeToString(cert.encoded)}\n-----END CERTIFICATE-----\n"
        val privateKeyPem = "-----BEGIN PRIVATE KEY-----\n${encoder.encodeToString(keyPair.private.encoded)}\n-----END PRIVATE KEY-----\n"
        val expiresAt = notAfter.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        return GeneratedCertificate(certPem, privateKeyPem, expiresAt)
    }
}
