package fr.gouv.agora.usecase.login

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.domain.UserInfo
import fr.gouv.agora.usecase.login.repository.UserDataRepository
import fr.gouv.agora.usecase.login.repository.UserRepository
import fr.gouv.agora.usecase.suspiciousUser.IsSuspiciousUserUseCase
import org.springframework.stereotype.Service

@Service
class LoginUseCase(
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository,
    private val isSuspiciousUserUseCase: IsSuspiciousUserUseCase,
) {

    fun findUser(userId: String): UserInfo? {
        return userRepository.getUserById(userId = userId)
    }

    fun login(loginRequest: LoginRequest): UserInfo? {
        return userRepository.getUserById(userId = loginRequest.userId)?.let {
            userDataRepository.addUserData(loginRequest = loginRequest)
            userRepository.updateUser(loginRequest = loginRequest)
        }
    }

    fun signUp(signupRequest: SignupRequest): UserInfo? {
        return userRepository.generateUser(signupRequest = signupRequest).also { userInfo ->
            userDataRepository.addUserData(signupRequest = signupRequest, generatedUserId = userInfo.userId)
            isSuspiciousUserUseCase.notifySignup(
                ipAddressHash = signupRequest.ipAddressHash,
                userAgent = signupRequest.userAgent,
            )
        }
    }

}