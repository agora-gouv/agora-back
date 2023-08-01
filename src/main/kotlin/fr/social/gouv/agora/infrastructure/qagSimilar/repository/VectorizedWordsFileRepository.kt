package fr.social.gouv.agora.infrastructure.qagSimilar.repository

import org.nd4j.linalg.api.ndarray.INDArray
import org.springframework.stereotype.Repository

@Repository
class VectorizedWordsFileRepository {

    fun getWordVectors(words: List<String>): Map<String, INDArray> {
        TODO("Not yet implemented")
    }
}