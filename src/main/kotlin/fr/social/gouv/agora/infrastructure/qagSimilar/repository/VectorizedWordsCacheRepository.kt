package fr.social.gouv.agora.infrastructure.qagSimilar.repository

import org.nd4j.linalg.api.ndarray.INDArray
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Repository

@Repository
class VectorizedWordsCacheRepository(private val cacheManager: CacheManager) {

    companion object {
        private const val WORD_VECTORS_CACHE_NAME = "wordVectorsCache"
        private const val WORD_VECTORS_CACHE_KEY = "wordVectorsCacheKey"
    }

    sealed class CacheResult {
        data class CachedWordVectorMap(val wordVectorMap: Map<String, INDArray?>) : CacheResult()
        object CacheNotInitialized : CacheResult()
    }

    @Suppress("UNCHECKED_CAST")
    fun getWordVectors(): CacheResult {
        val wordVectorMap = try {
            getCache()?.get(WORD_VECTORS_CACHE_KEY, List::class.java) as? Map<String, INDArray?>
        } catch (e: IllegalStateException) {
            null
        }
        return when (wordVectorMap) {
            null -> CacheResult.CacheNotInitialized
            else -> CacheResult.CachedWordVectorMap(wordVectorMap)
        }
    }

    fun addWordVectors(wordVectorMap: Map<String, INDArray?>) {
        val cachedWordVectors = when (val cacheResult = getWordVectors()) {
            CacheResult.CacheNotInitialized -> emptyMap()
            is CacheResult.CachedWordVectorMap -> cacheResult.wordVectorMap
        }
        getCache()?.put(WORD_VECTORS_CACHE_KEY, cachedWordVectors + wordVectorMap)
    }

    private fun getCache() = cacheManager.getCache(WORD_VECTORS_CACHE_NAME)

}