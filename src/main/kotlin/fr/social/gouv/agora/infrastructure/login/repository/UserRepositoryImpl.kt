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

    override fun generateUser(fcmToken: String): UserInfo {
        val userDTO = mapper.generateDto(fcmToken = fcmToken)
        val savedUserDTO = databaseRepository.save(userDTO)
        cacheRepository.insertUser(savedUserDTO)
        return mapper.toDomain(savedUserDTO)
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