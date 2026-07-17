package fr.gouv.agora.infrastructure.qagPaginated.repository

import fr.gouv.agora.domain.TrendingCluster
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiMetaPagination
import fr.gouv.agora.infrastructure.common.StrapiMetadata
import fr.gouv.agora.infrastructure.qagPaginated.repository.TrendingClusterCacheRepository.CacheResult
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class TrendingClusterRepositoryImplTest {

    @InjectMocks
    private lateinit var repository: TrendingClusterRepositoryImpl

    @Mock
    private lateinit var cacheRepository: TrendingClusterCacheRepository

    @Mock
    private lateinit var strapiRepository: TrendingClusterStrapiRepository

    private fun buildStrapiDTO(vararg dtos: TrendingClusterStrapiDTO): StrapiDTO<TrendingClusterStrapiDTO> {
        return StrapiDTO(
            data = dtos.toList(),
            meta = StrapiMetadata(StrapiMetaPagination(page = 1, pageSize = 100, pageCount = 1, total = dtos.size)),
        )
    }

    @Nested
    inner class GetClusters_WhenCacheHit {

        @Test
        fun `getClusters - when cache is populated - should return cached clusters without calling Strapi`() {
            // Given
            val cachedClusters = listOf(TrendingCluster(id = "tesla", mots = listOf("Tesla", "FSD")))
            given(cacheRepository.getClusters()).willReturn(CacheResult.CachedClusters(cachedClusters))

            // When
            val result = repository.getClusters()

            // Then
            assertThat(result).isEqualTo(cachedClusters)
            then(strapiRepository).shouldHaveNoInteractions()
        }
    }

    @Nested
    inner class GetClusters_WhenCacheMiss {

        @Test
        fun `getClusters - when cache is not initialized and Strapi returns valid clusters - should return clusters and insert in cache`() {
            // Given
            given(cacheRepository.getClusters()).willReturn(CacheResult.CacheNotInitialized)
            given(strapiRepository.getClusters()).willReturn(
                buildStrapiDTO(
                    TrendingClusterStrapiDTO(titre = "tesla", keywords = "Tesla, FSD, conduite autonome"),
                    TrendingClusterStrapiDTO(titre = "sante", keywords = "santé, médecin, hôpital"),
                )
            )

            // When
            val result = repository.getClusters()

            // Then
            assertThat(result).hasSize(2)
            assertThat(result[0]).isEqualTo(TrendingCluster(id = "tesla", mots = listOf("Tesla", "FSD", "conduite autonome")))
            assertThat(result[1]).isEqualTo(TrendingCluster(id = "sante", mots = listOf("santé", "médecin", "hôpital")))
            then(cacheRepository).should().insertClusters(result)
        }

        @Test
        fun `getClusters - when cache is not initialized and Strapi returns empty data - should return emptyList and not insert in cache`() {
            // Given
            given(cacheRepository.getClusters()).willReturn(CacheResult.CacheNotInitialized)
            given(strapiRepository.getClusters()).willReturn(StrapiDTO.ofEmpty())

            // When
            val result = repository.getClusters()

            // Then
            assertThat(result).isEmpty()
            then(cacheRepository).should().getClusters()
            then(cacheRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `getClusters - when Strapi returns a cluster with null keywords - should ignore that cluster silently`() {
            // Given
            given(cacheRepository.getClusters()).willReturn(CacheResult.CacheNotInitialized)
            given(strapiRepository.getClusters()).willReturn(
                buildStrapiDTO(
                    TrendingClusterStrapiDTO(titre = "tesla", keywords = "Tesla, FSD"),
                    TrendingClusterStrapiDTO(titre = "vide", keywords = null),
                )
            )

            // When
            val result = repository.getClusters()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo("tesla")
        }

        @Test
        fun `getClusters - when Strapi returns a cluster with blank keywords - should ignore that cluster silently`() {
            // Given
            given(cacheRepository.getClusters()).willReturn(CacheResult.CacheNotInitialized)
            given(strapiRepository.getClusters()).willReturn(
                buildStrapiDTO(
                    TrendingClusterStrapiDTO(titre = "tesla", keywords = "Tesla, FSD"),
                    TrendingClusterStrapiDTO(titre = "vide", keywords = "  ,  , "),
                )
            )

            // When
            val result = repository.getClusters()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].id).isEqualTo("tesla")
        }

        @Test
        fun `getClusters - when Strapi returns only clusters with empty keywords - should return emptyList and not insert in cache`() {
            // Given
            given(cacheRepository.getClusters()).willReturn(CacheResult.CacheNotInitialized)
            given(strapiRepository.getClusters()).willReturn(
                buildStrapiDTO(
                    TrendingClusterStrapiDTO(titre = "vide1", keywords = null),
                    TrendingClusterStrapiDTO(titre = "vide2", keywords = ""),
                )
            )

            // When
            val result = repository.getClusters()

            // Then
            assertThat(result).isEmpty()
            then(cacheRepository).should().getClusters()
            then(cacheRepository).shouldHaveNoMoreInteractions()
        }

        @Test
        fun `getClusters - when keywords contain extra spaces - should trim each keyword`() {
            // Given
            given(cacheRepository.getClusters()).willReturn(CacheResult.CacheNotInitialized)
            given(strapiRepository.getClusters()).willReturn(
                buildStrapiDTO(
                    TrendingClusterStrapiDTO(titre = "tesla", keywords = "  Tesla  ,  FSD  ,  conduite autonome  "),
                )
            )

            // When
            val result = repository.getClusters()

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].mots).containsExactly("Tesla", "FSD", "conduite autonome")
        }
    }
}
