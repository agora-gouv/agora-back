package fr.social.gouv.agora.usecase.qagSimilar.repository

import org.nd4j.linalg.api.ndarray.INDArray

interface VectorizedWordsRepository {
    fun getWordVectors(words: List<String>): Map<String, INDArray?>
}