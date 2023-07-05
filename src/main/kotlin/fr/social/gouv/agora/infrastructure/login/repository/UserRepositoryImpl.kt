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

    override fun getAllUsers(): List<UserInfo> {
        return getAllUserDTO().map(mapper::toDomain)
    }

    override fun getUserById(userId: String): UserInfo? {
        return try {
            val userUUID = UUID.fromString(userId)
            getAllUserDTO()
                .find { userDTO -> userDTO.id == userUUID }
                ?.let(mapper::toDomain)
        } catch (e: IllegalArgumentException) {
            null
        }
    }

    override fun updateUserFcmToken(userId: String, fcmToken: String): UserInfo? {

        return try {
            val userUUID = UUID.fromString(userId)
            val userDTO = findUserDTO(userUUID)
            if (userDTO != null) {
                val updatedDTO = updateUserIfRequired(userDTO, fcmToken)
                val usedUserDTO = updatedDTO ?: userDTO
                if (updatedDTO != null) {
                    cacheRepository.updateUser(usedUserDTO)
                }
                mapper.toDomain(usedUserDTO)
            } else {
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

    @Suppress("UNCHECKED_CAST")
    private fun getAllUserDTO() = when (val cacheResult = cacheRepository.getAllUserList()) {
        is CacheResult.CachedUserList -> cacheResult.allUserDTO
        CacheResult.CacheNotInitialized -> databaseRepository.findAll().also { allUserDTO ->
            cacheRepository.initializeCache(allUserDTO as List<UserDTO>)
        }
    }

    private fun findUserDTO(userUUID: UUID?) = getAllUserDTO().find { userDTO -> userDTO.id == userUUID }
}