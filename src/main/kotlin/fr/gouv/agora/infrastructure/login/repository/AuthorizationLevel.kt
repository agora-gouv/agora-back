package fr.gouv.agora.infrastructure.login.repository

enum class AuthorizationLevel(val value: Int) {
    DEFAULT_AUTHORIZATION_LEVEL(0),
    PUBLISHER_AUTHORIZATION_LEVEL(8),
    MODERATOR_AUTHORIZATION_LEVEL(42),
    ADMIN_AUTHORIZATION_LEVEL(1337),
}
