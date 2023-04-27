package fr.social.gouv.agora.infrastructure.login.repository

import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.infrastructure.login.dto.UserDTO
import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import org.springframework.stereotype.Component

@Component
class LoginRepositoryImpl(
    private val databaseRepository: LoginDatabaseRepository,
    private val cacheRepository: LoginCacheRepository,
    private val mapper: UserInfoMapper,
) : LoginRepository {

    companion object {
        private const val MAX_GENERATE_USER_RETRY_COUNT = 10
    }

    override fun loginOrRegister(deviceId: String, fcmToken: String): UserInfo? {
        val userDTO = cacheRepository.getUser(deviceId) ?: getUserFromDatabase(deviceId) ?: generateUser(
            deviceId = deviceId,
            fcmToken = fcmToken
        )
        return userDTO?.let { mapper.toDomain(userDTO) }
    }

    private fun getUserFromDatabase(deviceId: String): UserDTO? {
        return databaseRepository.getUser(deviceId)?.also { userDTO ->
            cacheRepository.insertUser(userDTO)
        }

    }

    private fun generateUser(deviceId: String, fcmToken: String): UserDTO? {
        repeat(MAX_GENERATE_USER_RETRY_COUNT) {
            val userDTO = mapper.generateDto(deviceId = deviceId, fcmToken = fcmToken)
            if (!databaseRepository.existsById(userDTO.id)) {
                cacheRepository.insertUser(userDTO)
                databaseRepository.save(userDTO)
                return userDTO
            }
        }
        return null
    }

}