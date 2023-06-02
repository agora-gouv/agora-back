package fr.social.gouv.agora.infrastructure.login.repository

import fr.social.gouv.agora.infrastructure.login.dto.UserDTO
import fr.social.gouv.agora.infrastructure.utils.UuidUtils
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class LoginCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        private const val USER_CACHE_NAME = "userCache"
    }

    sealed class CacheResult {
        data class CachedUser(val userDTO: UserDTO) : CacheResult()
        object CachedUserNotFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun getUserById(userId: UUID): CacheResult {
        return try {
            val cachedUserDTO = getUserCache()?.get(userId.toString(), UserDTO::class.java)
            when (cachedUserDTO?.id?.toString()) {
                null -> CacheResult.CacheNotInitialized
                UuidUtils.NOT_FOUND_UUID_STRING -> CacheResult.CachedUserNotFound
                else -> CacheResult.CachedUser(cachedUserDTO)
            }
        } catch (e: IllegalStateException) {
            CacheResult.CacheNotInitialized
        }
    }

    fun insertUser(userDTO: UserDTO) {
        getUserCache()?.put(userDTO.id, userDTO)
    }

    fun insertUserNotFound(userId: UUID) {
        getUserCache()?.put(userId.toString(), createUserNotFound())
    }

    private fun getUserCache() = cacheManager.getCache(USER_CACHE_NAME)

    private fun createUserNotFound(): UserDTO {
        return UserDTO(
            id = UuidUtils.NOT_FOUND_UUID,
            password = "",
            fcmToken = "",
            createdDate = Date(0),
            authorizationLevel = 0,
            isBanned = 0,
        )
    }

}