package fr.gouv.agora.usecase.login

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.domain.UserInfo
import fr.gouv.agora.infrastructure.utils.UuidUtils
import fr.gouv.agora.usecase.login.repository.BannedIpAddressHashRepository
import fr.gouv.agora.usecase.login.repository.UserDataRepository
import fr.gouv.agora.usecase.login.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class LoginUseCase(
    private val userRepository: UserRepository,
    private val userDataRepository: UserDataRepository,
    private val bannedIpAddressHashRepository: BannedIpAddressHashRepository,
) {

    fun findUser(userId: String): UserInfo? {
        return userRepository.getUserById(userId = userId)
    }

    fun login(loginRequest: LoginRequest): UserInfo? {
        if (isBannedIpAddressHash(loginRequest.ipAddressHash)) {
            userDataRepository.addUserData(loginRequest = loginRequest)
            return null
        }

        return userRepository.getUserById(userId = loginRequest.userId)?.let {
            userDataRepository.addUserData(loginRequest = loginRequest)
            userRepository.updateUser(loginRequest = loginRequest)
        }
    }

    fun signUp(signupRequest: SignupRequest): UserInfo? {
        if (isBannedIpAddressHash(signupRequest.ipAddressHash)) {
            userDataRepository.addUserData(
                signupRequest = signupRequest,
                generatedUserId = UuidUtils.NOT_FOUND_UUID_STRING,
            )
            return null
        }

        return userRepository.generateUser(signupRequest = signupRequest).also { userInfo ->
            userDataRepository.addUserData(signupRequest = signupRequest, generatedUserId = userInfo.userId)
        }
    }

    private fun isBannedIpAddressHash(ipAddressHash: String): Boolean {
        return bannedIpAddressHashRepository.getBannedIpAddressesHash().contains(ipAddressHash)
    }
}