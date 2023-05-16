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
        private const val DEVICE_ID_INDEX_CACHE_NAME = "deviceIdIndexCache"

        private const val DEVICE_ID_NOT_FOUND_USER_ID = ""
    }

    sealed class CacheResult {
        data class CachedUser(val userDTO: UserDTO) : CacheResult()
        object CachedUserNotFound : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun getUser(userId: UUID): CacheResult {
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

    fun getUserByDeviceId(deviceId: String): CacheResult {
        val userId = try {
            getDeviceIdIndexCache()?.get(deviceId, String::class.java)?.let(UUID::fromString)
        } catch (e: IllegalStateException) {
            null
        }

        return when (userId?.toString()) {
            null -> CacheResult.CacheNotInitialized
            DEVICE_ID_NOT_FOUND_USER_ID -> CacheResult.CachedUserNotFound
            else -> getUser(userId)
        }
    }

    fun insertUser(userDTO: UserDTO) {
        getUserCache()?.put(userDTO.id, userDTO)
        getDeviceIdIndexCache()?.put(userDTO.deviceId, userDTO.id)
    }

    fun insertUserNotFound(userId: UUID) {
        getUserCache()?.put(userId.toString(), createUserNotFound())
    }

    fun insertUserDeviceIdNotFound(deviceId: String) {
        getDeviceIdIndexCache()?.put(deviceId, DEVICE_ID_NOT_FOUND_USER_ID)
    }

    private fun getUserCache() = cacheManager.getCache(USER_CACHE_NAME)
    private fun getDeviceIdIndexCache() = cacheManager.getCache(DEVICE_ID_INDEX_CACHE_NAME)

    private fun createUserNotFound(): UserDTO {
        return UserDTO(
            id = UuidUtils.NOT_FOUND_UUID,
            deviceId = "",
            password = "",
            fcmToken = "",
            createdDate = Date(0),
            authorizationLevel = 0,
            isBanned = 0,
        )
    }

}