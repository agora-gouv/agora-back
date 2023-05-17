package fr.social.gouv.agora.infrastructure.login.repository

import fr.social.gouv.agora.domain.UserInfo
import fr.social.gouv.agora.infrastructure.login.dto.UserDTO
import fr.social.gouv.agora.infrastructure.login.repository.LoginCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.login.repository.UserRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserRepositoryImpl(
    private val databaseRepository: LoginDatabaseRepository,
    private val cacheRepository: LoginCacheRepository,
    private val mapper: UserInfoMapper,
) : UserRepository {

    companion object {
        private const val MAX_GENERATE_USER_RETRY_COUNT = 10
    }

    override fun getUserByDeviceId(deviceId: String): UserInfo? {
        return when (val cacheResult = cacheRepository.getUserByDeviceId(deviceId)) {
            CacheResult.CacheNotInitialized -> databaseRepository.getUserByDeviceId(deviceId).also { userDTO ->
                if (userDTO != null) {
                    cacheRepository.insertUser(userDTO)
                } else {
                    cacheRepository.insertUserDeviceIdNotFound(deviceId)
                }
            }
            CacheResult.CachedUserNotFound -> null
            is CacheResult.CachedUser -> cacheResult.userDTO
        }?.let { userDTO -> mapper.toDomain(userDTO) }
    }

    override fun getUserById(userId: String): UserInfo? {
        return try {
            val userUUID = UUID.fromString(userId)
            val (cachedUserDTO, databaseUserDTO) = when (val cacheResult = cacheRepository.getUserById(userUUID)) {
                CacheResult.CacheNotInitialized -> null to databaseRepository.getUserById(userUUID)
                CacheResult.CachedUserNotFound -> return null
                is CacheResult.CachedUser -> cacheResult.userDTO to null
            }

            val userDTO = cachedUserDTO ?: databaseUserDTO
            if (userDTO != null) {
                if (cachedUserDTO == null) {
                    cacheRepository.insertUser(userDTO)
                }
                mapper.toDomain(userDTO)
            } else {
                cacheRepository.insertUserNotFound(userUUID)
                null
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun updateUserFcmToken(userId: String, fcmToken: String): UserInfo? {
        return try {
            val userUUID = UUID.fromString(userId)
            val (cachedUserDTO, databaseUserDTO) = when (val cacheResult = cacheRepository.getUserById(userUUID)) {
                CacheResult.CacheNotInitialized -> null to databaseRepository.getUserById(userUUID)
                CacheResult.CachedUserNotFound -> return null
                is CacheResult.CachedUser -> cacheResult.userDTO to null
            }

            val userDTO = cachedUserDTO ?: databaseUserDTO
            if (userDTO != null) {
                val updatedDTO = updateUserIfRequired(userDTO, fcmToken)
                val usedUserDTO = updatedDTO ?: userDTO
                if (updatedDTO == null && cachedUserDTO == null) {
                    cacheRepository.insertUser(usedUserDTO)
                }
                mapper.toDomain(usedUserDTO)
            } else {
                cacheRepository.insertUserNotFound(userUUID)
                null
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun generateUser(deviceId: String, fcmToken: String): UserInfo? {
        val userDTO = generateUserDTO(deviceId = deviceId, fcmToken = fcmToken)
        return if (userDTO != null) {
            cacheRepository.insertUser(userDTO)
            databaseRepository.save(userDTO)
            mapper.toDomain(userDTO)
        } else {
            cacheRepository.insertUserDeviceIdNotFound(deviceId = deviceId)
            null
        }
    }

    fun generateUserDTO(deviceId: String, fcmToken: String): UserDTO? {
        repeat(MAX_GENERATE_USER_RETRY_COUNT) {
            val userDTO = mapper.generateDto(deviceId = deviceId, fcmToken = fcmToken)
            if (!databaseRepository.existsById(userDTO.id)) {
                return userDTO
            }
        }
        return null
    }

    private fun updateUserIfRequired(userDTO: UserDTO, fcmToken: String): UserDTO? {
        return if (userDTO.fcmToken != fcmToken) {
            val updatedUserDTO = mapper.updateDto(dto = userDTO, fcmToken = fcmToken)
            databaseRepository.save(updatedUserDTO)
            cacheRepository.insertUser(updatedUserDTO)
            updatedUserDTO
        } else null
    }

}