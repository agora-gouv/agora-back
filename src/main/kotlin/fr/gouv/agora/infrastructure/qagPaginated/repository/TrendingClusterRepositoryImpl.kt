package fr.gouv.agora.infrastructure.qagPaginated.repository

import fr.gouv.agora.domain.TrendingCluster
import fr.gouv.agora.infrastructure.qagPaginated.repository.TrendingClusterCacheRepository.CacheResult
import fr.gouv.agora.usecase.qagPaginated.repository.TrendingClusterRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class TrendingClusterRepositoryImpl(
    private val cacheRepository: TrendingClusterCacheRepository,
    private val strapiRepository: TrendingClusterStrapiRepository,
) : TrendingClusterRepository {

    private val logger = LoggerFactory.getLogger(TrendingClusterRepositoryImpl::class.java)

    override fun getClusters(): List<TrendingCluster> {
        return when (val cacheResult = cacheRepository.getClusters()) {
            is CacheResult.CachedClusters -> cacheResult.clusters
            CacheResult.CacheNotInitialized -> getClustersAndCacheIt()
        }
    }

    override fun clearCache() {
        cacheRepository.clearCache()
    }

    private fun getClustersAndCacheIt(): List<TrendingCluster> {
        val clusters = strapiRepository.getClusters().data.mapNotNull { dto ->
            val keywords = dto.keywords
                ?.split(",")
                ?.map { it.trim() }
                ?.filter { it.isNotBlank() }
                ?: emptyList()

            if (keywords.isEmpty()) {
                logger.warn("[TrendingCluster] ⚠️ Cluster '${dto.titre}' ignoré car sa liste de mots clés est vide")
                null
            } else {
                TrendingCluster(id = dto.titre, mots = keywords)
            }
        }

        if (clusters.isNotEmpty()) {
            cacheRepository.insertClusters(clusters)
        }

        logger.info("[TrendingCluster] ✅ ${clusters.size} clusters chargés depuis Strapi : ${clusters.map { "${it.id}(${it.mots.size} mots)" }}")
        return clusters
    }
}
