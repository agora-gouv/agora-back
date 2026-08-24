package fr.gouv.agora.infrastructure.acme.stub

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.util.Collections

@Component
@ConditionalOnProperty(name = ["ACME_STUB_MODE"], havingValue = "true")
class AcmeStubStore {

    data class StubCall(
        val timestamp: LocalDateTime,
        val method: String,
        val endpoint: String,
        val summary: String,
    )

    val calls: MutableList<StubCall> = Collections.synchronizedList(mutableListOf())

    var lastDeployedCertPreview: String? = null
    var lastDeployedAt: LocalDateTime? = null

    fun record(method: String, endpoint: String, summary: String) {
        calls += StubCall(
            timestamp = LocalDateTime.now(),
            method = method,
            endpoint = endpoint,
            summary = summary,
        )
    }

    fun reset() {
        calls.clear()
        lastDeployedCertPreview = null
        lastDeployedAt = null
    }
}
