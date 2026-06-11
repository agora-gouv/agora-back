package fr.gouv.agora.infrastructure.themeHebdo.repository

import fr.gouv.agora.domain.ThemeHebdo
import fr.gouv.agora.infrastructure.themeHebdo.repository.ThemeHebdoCacheRepository.CacheResult
import fr.gouv.agora.usecase.themeHebdo.repository.ThemeHebdoRepository
import org.springframework.stereotype.Component

@Component
class ThemeHebdoRepositoryImpl(
        private val cacheRepository: ThemeHebdoCacheRepository,
        private val strapiRepository: ThemeHebdoStrapiRepository,
        private val mapper: ThemeHebdoMapper,
) : ThemeHebdoRepository {

    companion object {
        private const val THEME_HEBDO_CACHE_ENABLED_KEY = "THEME_HEBDO_CACHE_ENABLED"
    }

    override fun getThemeHebdoList(): List<ThemeHebdo> {
        if (!isCacheEnabled()) {
            return strapiRepository.getThemeHebdo().let(mapper::toDomain)
        }

        return when (val cacheResult = cacheRepository.getThemeHebdoList()) {
            is CacheResult.CachedThemeHebdoList -> cacheResult.themeHebdoList
            is CacheResult.CacheNotInitialized -> getThemeHebdoListAndCacheIt()
        }
    }

    private fun isCacheEnabled(): Boolean {
        return System.getenv(THEME_HEBDO_CACHE_ENABLED_KEY).toBoolean()
    }

    private fun getThemeHebdoListAndCacheIt(): List<ThemeHebdo> {
        val themeHebdoList = strapiRepository.getThemeHebdo().let(mapper::toDomain)

        if (themeHebdoList.isNotEmpty()) {
            cacheRepository.insertThemeHebdoList(themeHebdoList)
        }

        return themeHebdoList
    }
}
