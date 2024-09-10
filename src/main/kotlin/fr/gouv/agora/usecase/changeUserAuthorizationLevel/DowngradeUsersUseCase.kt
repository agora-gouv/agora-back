package fr.gouv.agora.usecase.changeUserAuthorizationLevel

import fr.gouv.agora.infrastructure.login.repository.AuthorizationLevel
import fr.gouv.agora.usecase.login.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class DowngradeUsersUseCase(
    private val userRepository: UserRepository,
) {
    fun execute(usersId: List<String>): Int {
        val numberOfUpdatedUsers = userRepository.changeAuthorizationLevel(usersId, AuthorizationLevel.DEFAULT_AUTHORIZATION_LEVEL)
        return numberOfUpdatedUsers
    }
}
