package fr.social.gouv.agora.infrastructure.login.repository

import fr.social.gouv.agora.infrastructure.login.dto.UserDTO
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository
import java.util.*

@Repository
class LoginCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        private const val USER_CACHE_NAME = "userCache"
        private const val ALL_USER_CACHE_KEY = "userCacheList"
    }

    sealed class CacheResult {
        data class CachedUserList(val allUserDTO: List<UserDTO>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    fun initializeCache(allUserDTO: List<UserDTO>) {
        getUserCache()?.put(ALL_USER_CACHE_KEY, allUserDTO)
    }

    fun getAllUserList(): CacheResult {
        return when (val allUserDTO = getAllUserDTOFromCache()) {
            null -> CacheResult.CacheNotInitialized
            else -> CacheResult.CachedUserList(allUserDTO)
        }
    }

    @Throws(IllegalStateException::class)
    fun insertUser(userDTO: UserDTO) {
        getAllUserDTOFromCache()?.let { allUserDTO ->
            initializeCache(allUserDTO + userDTO)
        } ?: throw IllegalStateException("User cache has not been initialized")
    }

    @Throws(IllegalStateException::class)
    fun updateUser(userDTO: UserDTO) {
        getAllUserDTOFromCache()?.let { allUserDTO ->
            initializeCache(replaceUpdatedDTO(allUserDTO = allUserDTO, updatedUserDTO = userDTO))
        } ?: throw IllegalStateException("User cache has not been initialized")
    }

    private fun getUserCache() = cacheManager.getCache(USER_CACHE_NAME)

    @Suppress("UNCHECKED_CAST")
    private fun getAllUserDTOFromCache(): List<UserDTO>? {
        return try {
            getUserCache()?.get(ALL_USER_CACHE_KEY, List::class.java) as? List<UserDTO>
        } catch (e: IllegalStateException) {
            null
        }
    }

    private fun replaceUpdatedDTO(allUserDTO: List<UserDTO>, updatedUserDTO: UserDTO) = allUserDTO.map { userDTO ->
        if (userDTO.id == updatedUserDTO.id) updatedUserDTO
        else userDTO
    }

}