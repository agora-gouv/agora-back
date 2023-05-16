package fr.social.gouv.agora.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class UserInfoJwt(
    val userId: String,
    val isBanned: Boolean,
    val authorizationList: List<UserAuthorizationJWT>,
) : UserDetails {

    override fun getUsername(): String = userId
    override fun getPassword(): String = ""
    override fun isEnabled(): Boolean = true
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = !isBanned
    override fun isCredentialsNonExpired(): Boolean = true

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return authorizationList.toMutableList()
    }

}