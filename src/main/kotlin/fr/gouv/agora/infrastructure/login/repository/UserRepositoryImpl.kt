package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.domain.UserInfo
import fr.gouv.agora.infrastructure.login.dto.UserDTO
import fr.gouv.agora.infrastructure.login.repository.LoginCacheRepository.CacheResult
import fr.gouv.agora.usecase.login.repository.UserRepository
import org.springframework.stereotype.Component
import java.util.*

@Component
class UserRepositoryImpl(
    private val databaseRepository: LoginDatabaseRepository,
    private val cacheRepository: LoginCacheRepository,
    private val mapper: UserInfoMapper,
) : UserRepository {

    override fun getAllUsers(): List<UserInfo> {
        return databaseRepository.findAll().map(mapper::toDomain)
    }

    override fun getUserById(userId: String): UserInfo? {
        return try {
            getUserDTO(UUID.fromString(userId))
                ?.let(mapper::toDomain)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun updateUser(userId: String, fcmToken: String): UserInfo? {
        return try {
            getUserDTO(UUID.fromString(userId))?.let { userDTO ->
                val updatedUserDTO = mapper.updateDto(dto = userDTO, fcmToken = fcmToken)
                val savedUserDTO = databaseRepository.save(updatedUserDTO)
                cacheRepository.updateUser(savedUserDTO)
                mapper.toDomain(savedUserDTO)
            }
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun generateUser(fcmToken: String): UserInfo {
        val userDTO = mapper.generateDto(fcmToken = fcmToken)
        val savedUserDTO = databaseRepository.save(userDTO)
        cacheRepository.insertUser(savedUserDTO)
        return mapper.toDomain(savedUserDTO)
    }

    private fun getUserDTO(userUUID: UUID): UserDTO? {
        return when (val cacheResult = cacheRepository.getUser(userUUID)) {
            is CacheResult.CachedUser -> cacheResult.userDTO
            CacheResult.CacheUserNotFound -> null
            CacheResult.CacheNotInitialized -> databaseRepository.getUserById(userUUID).also { userDTO ->
                when (userDTO) {
                    null -> cacheRepository.insertUserNotFound(userUUID)
                    else -> cacheRepository.insertUser(userDTO)
                }
            }
        }
    }

}