package fr.gouv.agora.security

import org.springframework.security.core.GrantedAuthority

enum class UserAuthorizationJWT(private val authorityValue: String) : GrantedAuthority {
    VIEW_CONSULTATION("VIEW_CONSULTATION"),
    VIEW_UNPUBLISHED_CONSULTATION("VIEW_UNPUBLISHED_CONSULTATION"),
    ANSWER_CONSULTATION("ANSWER_CONSULTATION"),
    VIEW_QAG("VIEW_QAG"),
    SUPPORT_QAG("SUPPORT_QAG"),
    FEEDBACK_QAG_RESPONSE("FEEDBACK_QAG_RESPONSE"),
    ADD_QAG("ADD_QAG"),
    MODERATE_QAG("MODERATE_QAG"),
    ADMIN_APIS("ADMIN_APIS"),
    ;

    override fun getAuthority() = authorityValue
}
