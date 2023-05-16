package fr.social.gouv.agora.security

import org.springframework.security.core.GrantedAuthority

enum class UserAuthorizationJWT(private val authorityValue: String) : GrantedAuthority {
    VIEW_CONSULTATION("VIEW_CONSULTATION"),
    ANSWER_CONSULTATION("ANSWER_CONSULTATION"),
    VIEW_QAG("VIEW_QAG"),
    SUPPORT_QAG("SUPPORT_QAG"),
    FEEDBACK_QAG_RESPONSE("FEEDBACK_QAG_RESPONSE"),
    ADD_QAG("ADD_QAG"),
    MODERATE_QAG("MODERATE_QAG"),
    ADMIN_API("ADMIN_API"),
    ;

    override fun getAuthority() = authorityValue

}