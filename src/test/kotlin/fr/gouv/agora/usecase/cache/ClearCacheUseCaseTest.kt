package fr.gouv.agora.usecase.cache

import fr.gouv.agora.usecase.qagPaginated.repository.TrendingClusterRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.cache.Cache
import org.springframework.cache.CacheManager

@ExtendWith(MockitoExtension::class)
class ClearCacheUseCaseTest {

    @Mock
    private lateinit var cacheManager: CacheManager

    @Mock
    private lateinit var shortTermCacheManager: CacheManager

    @Mock
    private lateinit var longTermCacheManager: CacheManager

    @Mock
    private lateinit var eternalCacheManager: CacheManager

    @Mock
    private lateinit var trendingClusterRepository: TrendingClusterRepository

    private lateinit var useCase: ClearCacheUseCase

    @BeforeEach
    fun setUp() {
        useCase = ClearCacheUseCase(
            cacheManager,
            shortTermCacheManager,
            longTermCacheManager,
            eternalCacheManager,
            trendingClusterRepository,
        )
    }

    @Nested
    inner class `clearAllCaches - when all managers have caches` {

        @Test
        fun `clearAllCaches - when all managers have caches - should clear each cache and return their names`() {
            // Given
            val cache1 = mock(Cache::class.java)
            val cache2 = mock(Cache::class.java)
            val cache3 = mock(Cache::class.java)

            given(cacheManager.cacheNames).willReturn(setOf("cache-a", "cache-b"))
            given(cacheManager.getCache("cache-a")).willReturn(cache1)
            given(cacheManager.getCache("cache-b")).willReturn(cache2)

            given(shortTermCacheManager.cacheNames).willReturn(setOf("short-cache"))
            given(shortTermCacheManager.getCache("short-cache")).willReturn(cache3)

            given(longTermCacheManager.cacheNames).willReturn(emptySet())
            given(eternalCacheManager.cacheNames).willReturn(emptySet())

            // When
            val result = useCase.clearAllCaches()

            // Then
            assertThat(result).containsExactlyInAnyOrder("cache-a", "cache-b", "short-cache", "trendingClusterCache")
            then(cache1).should().clear()
            then(cache2).should().clear()
            then(cache3).should().clear()
            then(trendingClusterRepository).should().clearCache()
        }
    }

    @Nested
    inner class `clearAllCaches - when all managers are empty` {

        @Test
        fun `clearAllCaches - when no caches exist - should return only trendingClusterCache`() {
            // Given
            given(cacheManager.cacheNames).willReturn(emptySet())
            given(shortTermCacheManager.cacheNames).willReturn(emptySet())
            given(longTermCacheManager.cacheNames).willReturn(emptySet())
            given(eternalCacheManager.cacheNames).willReturn(emptySet())

            // When
            val result = useCase.clearAllCaches()

            // Then
            assertThat(result).containsExactly("trendingClusterCache")
            then(trendingClusterRepository).should().clearCache()
        }
    }

    @Nested
    inner class `clearAllCaches - when getCache returns null` {

        @Test
        fun `clearAllCaches - when getCache returns null - should still include the cache name in result`() {
            // Given
            val cache = mock(Cache::class.java)
            given(cacheManager.cacheNames).willReturn(setOf("cache-present", "cache-null"))
            given(cacheManager.getCache("cache-present")).willReturn(cache)
            given(cacheManager.getCache("cache-null")).willReturn(null)

            given(shortTermCacheManager.cacheNames).willReturn(emptySet())
            given(longTermCacheManager.cacheNames).willReturn(emptySet())
            given(eternalCacheManager.cacheNames).willReturn(emptySet())

            // When
            val result = useCase.clearAllCaches()

            // Then
            assertThat(result).containsExactlyInAnyOrder("cache-present", "cache-null", "trendingClusterCache")
            then(cache).should().clear()
            then(trendingClusterRepository).should().clearCache()
        }
    }

    @Nested
    inner class `clearAllCaches - trending cluster cache` {

        @Test
        fun `clearAllCaches - should always call clearCache on trendingClusterRepository and include it in result`() {
            // Given
            given(cacheManager.cacheNames).willReturn(emptySet())
            given(shortTermCacheManager.cacheNames).willReturn(emptySet())
            given(longTermCacheManager.cacheNames).willReturn(emptySet())
            given(eternalCacheManager.cacheNames).willReturn(emptySet())

            // When
            val result = useCase.clearAllCaches()

            // Then
            assertThat(result).contains("trendingClusterCache")
            then(trendingClusterRepository).should().clearCache()
        }
    }
}
