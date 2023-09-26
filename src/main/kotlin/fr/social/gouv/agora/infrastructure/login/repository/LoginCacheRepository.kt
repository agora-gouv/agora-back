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
        object CacheUserNotFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun getUser(userUUID: UUID): CacheResult {
        val userDTO = getCache()?.get(userUUID.toString(), UserDTO::class.java)
        return when (userDTO?.id) {
            null -> CacheResult.CacheNotInitialized
            UuidUtils.NOT_FOUND_UUID -> CacheResult.CacheUserNotFound
            else -> CacheResult.CachedUser(userDTO)
        }
    }

    fun insertUser(userDTO: UserDTO) {
        getCache()?.put(userDTO.id.toString(), userDTO)
    }

    fun updateUser(userDTO: UserDTO) {
        insertUser(userDTO)
    }

    fun insertUserNotFound(userUUID: UUID) {
        getCache()?.put(userUUID.toString(), createNotFoundUser())
    }

    private fun getCache() = cacheManager.getCache(USER_CACHE_NAME)

    private fun createNotFoundUser(): UserDTO {
        return UserDTO(
            id = UuidUtils.NOT_FOUND_UUID,
            password = "",
            fcmToken = "",
            createdDate = Date(0),
            authorizationLevel = 0,
            isBanned = 0,
            lastConnectionDate = Date(0),
        )
    }

}