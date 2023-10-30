package fr.social.gouv.agora.infrastructure.qagSimilar.repository

//@Component
//class VectorizedWordsRepositoryImpl(
//    private val cacheRepository: VectorizedWordsCacheRepository,
//    private val fileRepository: VectorizedWordsFileRepository,
//) : VectorizedWordsRepository {
//
//    override fun getWordVectors(words: List<String>): Map<String, INDArray?> {
//        val cachedWordVectors = cacheRepository.getWordVectors(words)
//
//        val knownCachedWordVectors = cachedWordVectors
//            .filterValues { cacheResult -> cacheResult is CachedWordVector || cacheResult is WordWithoutVector }
//            .map { (word, cacheResult) ->
//                when (cacheResult) {
//                    is CachedWordVector -> word to cacheResult.wordVector
//                    WordWithoutVector -> word to null
//                    CacheNotInitialized -> throw IllegalStateException("Cannot map CacheNotInitialized vectors")
//                }
//            }.toMap()
//
//        if (knownCachedWordVectors.size == words.size) {
//            return knownCachedWordVectors
//        }
//
//        val missingWordVectors = words.filter { word -> !knownCachedWordVectors.containsKey(word) }
//        val wordVectorsFromFile = fileRepository.getWordVectors(missingWordVectors)
//        val stillMissingWordVectors = words.filter { word ->
//            !knownCachedWordVectors.containsKey(word) && !wordVectorsFromFile.containsKey(word)
//        }
//
//        val addToCacheVectors = wordVectorsFromFile
//            .filterValues { vectorResult -> vectorResult is VectorResult.VectorFound }
//            .map { (word, vectorResult) ->
//                word to when (vectorResult) {
//                    is VectorResult.VectorFound -> vectorResult.vector
//                    VectorResult.VectorError -> throw Exception("Cannot map VectorError to a vector")
//                }
//            }.toMap() + stillMissingWordVectors.associateWith { null }
//        if (addToCacheVectors.isNotEmpty()) {
//            cacheRepository.insertWordVectors(addToCacheVectors)
//        }
//
//        return knownCachedWordVectors + addToCacheVectors
//    }
//
//}