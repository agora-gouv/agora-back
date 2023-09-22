package fr.social.gouv.agora.usecase.qagSimilar

//@Component
//class QagSimilarityCalculator {
//
//    fun calculateSimilarity(qagWordVectors: List<INDArray>, writingQagWordVectors: List<INDArray>): Double {
//        return cosineSimilarity(
//            qagSentenceVector = toSentenceVector(qagWordVectors),
//            writingQagSentenceVector = toSentenceVector(writingQagWordVectors),
//        )
//    }
//
//    private fun toSentenceVector(wordVectors: List<INDArray>): INDArray {
//        val vectorSum = Nd4j.create(wordVectors.first().size(0))
//
//        wordVectors.forEach { wordVector -> vectorSum.addi(wordVector) }
//        vectorSum.divi(wordVectors.size)
//        return Transforms.unitVec(vectorSum)
//    }
//
//    private fun cosineSimilarity(qagSentenceVector: INDArray, writingQagSentenceVector: INDArray): Double {
//        return Transforms.cosineSim(qagSentenceVector, writingQagSentenceVector) * 100
//    }
//}