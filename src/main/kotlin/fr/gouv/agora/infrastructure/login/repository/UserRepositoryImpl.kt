package fr.gouv.agora.infrastructure.login.repository

import fr.gouv.agora.domain.LoginRequest
import fr.gouv.agora.domain.SignupRequest
import fr.gouv.agora.domain.UserInfo
import fr.gouv.agora.infrastructure.login.dto.UserDTO
import fr.gouv.agora.infrastructure.login.repository.LoginCacheRepository.CacheResult
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
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

    override fun updateUser(loginRequest: LoginRequest): UserInfo? {
        return loginRequest.userId.toUuidOrNull()?.let { userUUID ->
            getUserDTO(userUUID = userUUID)?.let { userDTO ->
                val updatedUserDTO = mapper.updateDto(dto = userDTO, loginRequest = loginRequest)
                val savedUserDTO = databaseRepository.save(updatedUserDTO)
                cacheRepository.updateUser(savedUserDTO)
                mapper.toDomain(savedUserDTO)
            }
        }
    }

    override fun generateUser(signupRequest: SignupRequest): UserInfo {
        val userDTO = mapper.generateDto(signupRequest = signupRequest)
        val savedUserDTO = databaseRepository.save(userDTO)
        cacheRepository.insertUser(savedUserDTO)
        return mapper.toDomain(savedUserDTO)
    }

    override fun getUsersNotAnsweredConsultation(consultationId: String): List<UserInfo> {
        return consultationId.toUuidOrNull()?.let { consultationUUID ->
            databaseRepository.getUsersNotAnsweredConsultation(consultationUUID).map(mapper::toDomain)
        } ?: emptyList()
    }

    override fun deleteUsers(userIDs: List<String>) {
        val userUUIDs = userIDs.mapNotNull { it.toUuidOrNull() }
        databaseRepository.deleteUsers(userUUIDs)
        userUUIDs.forEach { userUUID ->
            cacheRepository.deleteUser(userUUID)
        }
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