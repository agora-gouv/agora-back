package fr.gouv.agora.infrastructure.themeHebdo.repository

import fr.gouv.agora.domain.ThematiqueHebdo
import fr.gouv.agora.infrastructure.themeHebdo.repository.ThemeHebdoCacheRepository.CacheResult
import fr.gouv.agora.usecase.themeHebdo.repository.ThemeHebdoRepository
import org.springframework.stereotype.Component

@Component
class ThemeHebdoRepositoryImpl(
        private val cacheRepository: ThemeHebdoCacheRepository,
        private val strapiRepository: ThemeHebdoStrapiRepository,
        private val mapper: ThemeHebdoMapper,
) : ThemeHebdoRepository {

    override fun getThemeHebdoList(): List<ThematiqueHebdo> =
            when (val cacheResult = cacheRepository.getThemeHebdoList()) {
                is CacheResult.CachedThemeHebdoList -> cacheResult.themeHebdoList
                is CacheResult.CacheNotInitialized -> getThemeHebdoListAndCacheIt()
            }

    private fun getThemeHebdoListAndCacheIt(): List<ThematiqueHebdo> {
        val themeHebdoList = strapiRepository.getThemeHebdo().let(mapper::toDomain)

        if (themeHebdoList.isNotEmpty()) {
            cacheRepository.insertThemeHebdoList(themeHebdoList)
        }

        return themeHebdoList
    }
}
