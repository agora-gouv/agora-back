package fr.social.gouv.agora.infrastructure.qagSimilar.repository

//@Repository
//class VectorizedWordsCacheRepository(private val cacheManager: CacheManager) {
//
//    companion object {
//        private const val WORD_VECTORS_CACHE_NAME = "wordVectorsCache"
//        private val NO_WORD_VECTOR = VectorizedWord(data = FloatArray(0), shape = IntArray(0))
//    }
//
//    sealed class CacheResult {
//        data class CachedWordVector(val wordVector: INDArray) : CacheResult()
//        object WordWithoutVector : CacheResult()
//        object CacheNotInitialized : CacheResult()
//    }
//
//    @Suppress("UNCHECKED_CAST")
//    fun getWordVectors(words: List<String>): Map<String, CacheResult> {
//        return words.associateWith { word ->
//            when (val vectorString = getCache()?.get(word, VectorizedWord::class.java)) {
//                null -> CacheResult.CacheNotInitialized
//                NO_WORD_VECTOR -> CacheResult.WordWithoutVector
//                else -> CacheResult.CachedWordVector(cacheToVector(vectorString))
//            }
//        }
//    }
//
//    fun insertWordVectors(wordVectorMap: Map<String, INDArray?>) {
//        wordVectorMap.forEach { (word, vector) ->
//            getCache()?.put(word, vectorToCache(vector))
//        }
//    }
//
//    private fun getCache() = cacheManager.getCache(WORD_VECTORS_CACHE_NAME)
//
//    private fun vectorToCache(vector: INDArray?) = vector?.let {
//        VectorizedWord(
//            data = vector.toFloatVector(),
//            shape = vector.shape().map { it.toInt() }.toIntArray(),
//        )
//    } ?: NO_WORD_VECTOR
//
//    private fun cacheToVector(vectorizedWord: VectorizedWord) =
//        NDArray(
//            vectorizedWord.data,
//            vectorizedWord.shape,
//        )
//}
//
//data class VectorizedWord(
//    @JsonProperty("data")
//    val data: FloatArray,
//    @JsonProperty("shape")
//    val shape: IntArray,
//) {
//    override fun equals(other: Any?): Boolean {
//        if (this === other) return true
//        if (javaClass != other?.javaClass) return false
//
//        other as VectorizedWord
//
//        if (!data.contentEquals(other.data)) return false
//        if (!shape.contentEquals(other.shape)) return false
//
//        return true
//    }
//
//    override fun hashCode(): Int {
//        var result = data.contentHashCode()
//        result = 31 * result + shape.contentHashCode()
//        return result
//    }
//}
