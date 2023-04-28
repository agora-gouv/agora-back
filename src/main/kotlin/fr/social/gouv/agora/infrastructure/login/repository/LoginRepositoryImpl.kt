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
        val cachedUserDTO = cacheRepository.getUser(deviceId)
        val userDTO = cachedUserDTO ?: databaseRepository.getUser(deviceId) ?: generateUser(
            deviceId = deviceId,
            fcmToken = fcmToken
        )
        return userDTO?.let {
            val updatedUserDTO = updateUserIfRequired(userDTO, fcmToken)
            val usedUserDTO = updatedUserDTO ?: userDTO

            if (cachedUserDTO == null || updatedUserDTO != null) {
                cacheRepository.insertUser(usedUserDTO)
            }
            mapper.toDomain(usedUserDTO)
        }
    }

    private fun generateUser(deviceId: String, fcmToken: String): UserDTO? {
        repeat(MAX_GENERATE_USER_RETRY_COUNT) {
            val userDTO = mapper.generateDto(deviceId = deviceId, fcmToken = fcmToken)
            if (!databaseRepository.existsById(userDTO.id)) {
                databaseRepository.save(userDTO)
                return userDTO
            }
        }
        return null
    }

    private fun updateUserIfRequired(userDTO: UserDTO, fcmToken: String): UserDTO? {
        return if (userDTO.fcmToken != fcmToken) {
            val updatedUserDTO = mapper.updateDto(dto = userDTO, fcmToken = fcmToken)
            databaseRepository.save(updatedUserDTO)
            updatedUserDTO
        } else null
    }

}