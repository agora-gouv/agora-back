package fr.social.gouv.agora.infrastructure.qagSimilar.repository

import fr.social.gouv.agora.infrastructure.qagSimilar.repository.VectorizedWordsCacheRepository.CacheResult
import fr.social.gouv.agora.usecase.qagSimilar.repository.VectorizedWordsRepository
import org.nd4j.linalg.api.ndarray.INDArray
import org.springframework.stereotype.Component

@Component
class VectorizedWordsRepositoryImpl(
    private val cacheRepository: VectorizedWordsCacheRepository,
    private val fileRepository: VectorizedWordsFileRepository,
) : VectorizedWordsRepository {

    override fun getWordVectors(words: List<String>): Map<String, INDArray?> {
        val cachedWordVectors = when (val cacheResult = cacheRepository.getWordVectors()) {
            CacheResult.CacheNotInitialized -> emptyMap()
            is CacheResult.CachedWordVectorMap -> cacheResult.wordVectorMap
        }

        val filteredWordVectors = cachedWordVectors.filter { (word, _) -> words.contains(word) }
        val missingWordVectors = words.filterNot { word -> cachedWordVectors.contains(word) }
        if (missingWordVectors.isEmpty()) {
            return filteredWordVectors
        }

        val wordVectorsFromFile = fileRepository.getWordVectors(missingWordVectors)
        val stillMissingWordVectors = missingWordVectors.filterNot { word -> wordVectorsFromFile.containsKey(word) }
        val newWordVectors = filteredWordVectors + wordVectorsFromFile + stillMissingWordVectors.associateWith { null }
        cacheRepository.addWordVectors(newWordVectors)

        return newWordVectors
    }

}