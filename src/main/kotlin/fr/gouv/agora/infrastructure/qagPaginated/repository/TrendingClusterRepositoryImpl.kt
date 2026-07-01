package fr.gouv.agora.infrastructure.qagPaginated.repository

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import fr.gouv.agora.domain.TrendingCluster
import fr.gouv.agora.usecase.qagPaginated.repository.TrendingClusterRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository

@Repository
class TrendingClusterRepositoryImpl(private val objectMapper: ObjectMapper) : TrendingClusterRepository {

    companion object {
        private const val CLUSTERS_FILE = "/trending_clusters.json"
    }

    private val logger = LoggerFactory.getLogger(TrendingClusterRepositoryImpl::class.java)

    private val cachedClusters: List<TrendingCluster> by lazy {
        val resource = javaClass.getResourceAsStream(CLUSTERS_FILE)
        if (resource == null) {
            logger.warn("[TrendingCluster] ⚠️ Fichier $CLUSTERS_FILE introuvable — aucun cluster chargé")
            return@lazy emptyList()
        }
        val dtos: List<TrendingClusterDto> = objectMapper.readValue(resource)
        val clusters = dtos.map { TrendingCluster(id = it.id, mots = it.mots) }
        logger.info("[TrendingCluster] ✅ ${clusters.size} clusters chargés : ${clusters.map { "${it.id}(${it.mots.size} mots)" }}")
        clusters
    }

    override fun getClusters(): List<TrendingCluster> = cachedClusters

    private data class TrendingClusterDto(
        @JsonProperty("id") val id: String,
        @JsonProperty("mots") val mots: List<String>,
    )
}
