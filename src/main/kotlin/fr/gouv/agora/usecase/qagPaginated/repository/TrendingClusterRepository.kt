package fr.gouv.agora.usecase.qagPaginated.repository

import fr.gouv.agora.domain.TrendingCluster

interface TrendingClusterRepository {
    fun getClusters(): List<TrendingCluster>
}
