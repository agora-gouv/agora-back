package fr.social.gouv.agora.infrastructure.login.repository

import fr.social.gouv.agora.infrastructure.login.dto.UserDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class LoginCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        private const val USER_CACHE_NAME = "userCache"
    }

    fun getUser(deviceId: String): UserDTO? {
        return try {
            getCache()?.get(deviceId, UserDTO::class.java)
        } catch (e: IllegalStateException) {
            null
        }
    }

    fun insertUser(userDTO: UserDTO) {
        getCache()?.put(userDTO.deviceId, userDTO)
    }

    private fun getCache() = cacheManager.getCache(USER_CACHE_NAME)

}