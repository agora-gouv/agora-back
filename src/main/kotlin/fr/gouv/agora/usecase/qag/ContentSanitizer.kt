package fr.gouv.agora.usecase.qag

import org.owasp.html.HtmlPolicyBuilder
import org.springframework.stereotype.Component
import org.springframework.web.util.HtmlUtils

@Component
class ContentSanitizer {

    fun sanitize(content: String, maxLength: Int): String {
        val policyFactory = HtmlPolicyBuilder().toFactory()
        return HtmlUtils.htmlUnescape(policyFactory.sanitize(content)).take(maxLength)
    }

}