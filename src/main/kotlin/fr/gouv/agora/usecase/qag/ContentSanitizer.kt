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

    fun sanitizeRichText(content: String, maxLength: Int): String {
        val policyFactory = HtmlPolicyBuilder()
            .allowElements("p", "b", "i", "u", "del", "code", "h1", "h2", "h3", "h4", "h5", "h6", "ul", "ol", "li", "blockquote")
            .allowElements("a").allowAttributes("href").onElements("a")
            .toFactory()
        return HtmlUtils.htmlUnescape(policyFactory.sanitize(content)).take(maxLength)
    }

}