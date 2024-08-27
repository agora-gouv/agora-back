package fr.gouv.agora.config

import fr.gouv.agora.security.UserAuthorizationJWT
import fr.gouv.agora.security.UserInfoJwt
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthentificationHelper {
    fun getUserId(): String? {
        return (SecurityContextHolder.getContext().authentication.principal as? UserInfoJwt)?.userId
    }

    fun canViewUnpublishedConsultations(): Boolean {
        val userInfoJwt = SecurityContextHolder.getContext().authentication.principal as? UserInfoJwt
            ?: return false

        return userInfoJwt.authorities.contains(UserAuthorizationJWT.VIEW_UNPUBLISHED_CONSULTATION)
    }
}
