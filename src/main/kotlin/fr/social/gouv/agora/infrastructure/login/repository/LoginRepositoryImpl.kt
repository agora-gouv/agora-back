package fr.social.gouv.agora.infrastructure.login.repository

import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.infrastructure.login.dto.UserDTO
import fr.social.gouv.agora.infrastructure.login.repository.LoginCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.login.repository.LoginRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class LoginRepositoryImpl(
    private val databaseRepository: LoginDatabaseRepository,
    private val cacheRepository: LoginCacheRepository,
    private val mapper: UserInfoMapper,
) : LoginRepository {

    companion object {
        private const val MAX_GENERATE_USER_RETRY_COUNT = 10
    }

    override fun getUser(userId: String): UserInfo? {
        return try {
            UUID.fromString(userId)
            when (val cacheResult = cacheRepository.getUser(userId)) {
                CacheResult.CacheNotInitialized -> databaseRepository.getUserById(userId).also { userDTO ->
                    insertUserToCache(userId, userDTO)
                }
                CacheResult.CachedUserNotFound -> null
                is CacheResult.CachedUser -> cacheResult.userDTO
            }?.let { userDTO -> mapper.toDomain(userDTO) }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun loginOrRegister(deviceId: String, fcmToken: String): UserInfo? {
        val (cachedUserDTO, databaseUserDTO) = when (val cacheResult = cacheRepository.getUserByDeviceId(deviceId)) {
            CacheResult.CacheNotInitialized -> null to databaseRepository.getUserByDeviceId(deviceId)
            CacheResult.CachedUserNotFound -> null to null
            is CacheResult.CachedUser -> cacheResult.userDTO to null
        }

        val userDTO = cachedUserDTO ?: databaseUserDTO ?: generateUser(
            deviceId = deviceId,
            fcmToken = fcmToken
        )

        return if (userDTO != null) {
            val updatedUserDTO = updateUserIfRequired(userDTO, fcmToken)
            val usedUserDTO = updatedUserDTO ?: userDTO

            if (cachedUserDTO == null || updatedUserDTO != null) {
                cacheRepository.insertUser(usedUserDTO)
            }
            mapper.toDomain(usedUserDTO)
        } else {
            cacheRepository.insertUserDeviceIdNotFound(deviceId = deviceId)
            null
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

    private fun insertUserToCache(userId: String, userDTO: UserDTO?) {
        if (userDTO != null) {
            cacheRepository.insertUser(userDTO)
        } else {
            cacheRepository.insertUserNotFound(userId)
        }
    }

}