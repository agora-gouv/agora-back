package fr.social.gouv.agora.usecase.qag

import fr.social.gouv.agora.domain.QagModerating
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.qag.repository.QagInfo
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import fr.social.gouv.agora.usecase.thematique.repository.ThematiqueRepository
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.ops.transforms.Transforms
import org.springframework.stereotype.Service
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream

@Service
class FindSimilarQagUseCase(
    private val qagInfoRepository: QagInfoRepository,
    private val sentenceNormalizer: SentenceNormalizer,
    private val mapper: QagModeratingMapper,
    private val thematiqueRepository: ThematiqueRepository,
    private val supportRepository: GetSupportQagRepository,
) {
    companion object {
        private const val MODEL_PATH_FILE = "cc.fr.300.vec.gz"
        private const val BATCH_SIZE = 1000
        private const val MIN_SCORE = 75
    }

    fun findSimilarQag(question: String, userId: String): List<QagModerating> {
        val allQagAcceptedList =
            qagInfoRepository.getAllQagInfo().filter { qagInfo -> qagInfo.status == QagStatus.MODERATED_ACCEPTED }

        val qagSimilarityScores = mutableListOf<Map<Double, QagInfo>>()
        for (qag in allQagAcceptedList) {
            val score =
                similarity(sentenceNormalizer.preProcess(question), sentenceNormalizer.preProcess(qag.title))
            if (score > MIN_SCORE)
                qagSimilarityScores.add(mapOf(score to qag))
        }
        qagSimilarityScores.sortByDescending { it.keys.first() }
        val qagInfoMostSimilarList = qagSimilarityScores.take(3).map { it.values.first() }
        val qagModeratingMostSimilarList = mutableListOf<QagModerating>()
        for (qagInfo in qagInfoMostSimilarList)
            thematiqueRepository.getThematique(qagInfo.thematiqueId)?.let { thematique ->
                supportRepository.getSupportQag(qagId = qagInfo.id, userId = userId)?.let { supportQag ->
                    mapper.toQagModerating(
                        qagInfo = qagInfo,
                        thematique = thematique,
                        supportQag = supportQag
                    )
                }
            }?.let {
                qagModeratingMostSimilarList.add(
                    it
                )
            }
        return qagModeratingMostSimilarList
    }

    private fun similarity(sentence1: String, sentence2: String): Double {
        val tokenizerFactory = DefaultTokenizerFactory()
        val sentence1Tokens = sentenceToTokens(sentence1, tokenizerFactory)
        val sentence2Tokens = sentenceToTokens(sentence2, tokenizerFactory)
        val sumTokens = (sentence1Tokens + sentence2Tokens).distinct()

        val tokenVectorsMapList = mutableListOf<Map<String, INDArray>>()

        GZIPInputStream(this.javaClass.classLoader.getResourceAsStream(MODEL_PATH_FILE)).use { gzipInputStream ->
            val reader = InputStreamReader(gzipInputStream, "UTF-8")
            val bufferedReader = BufferedReader(reader)

            var fileCounter = 0
            val lines = mutableListOf<String>()

            bufferedReader.forEachLine loop@{ line ->
                if (tokenVectorsMapList.size < sumTokens.size) {
                    lines.add(line)
                    if (lines.size == BATCH_SIZE) {
                        processBatch(lines, fileCounter++, sumTokens, tokenVectorsMapList)
                        lines.clear()
                    }
                } else {
                    lines.clear()
                    return@loop
                }
            }

            // Traitement des lignes restantes dont le nombre est < 100
            if (lines.isNotEmpty()) {
                processBatch(lines, fileCounter++, sumTokens, tokenVectorsMapList)
            }
        }
        val sentence1Vector = tokensToVector(sentence1Tokens, tokenVectorsMapList)
        val sentence2Vector = tokensToVector(sentence2Tokens, tokenVectorsMapList)
        return cosineSimilarity(sentence1Vector, sentence2Vector)
    }

    private fun sentenceToTokens(sentence: String, tokenizerFactory: TokenizerFactory): List<String> {
        val tokenizer = tokenizerFactory.create(sentence)
        return tokenizer.tokens
    }

    private fun processBatch(
        lines: List<String>,
        batchNumber: Int,
        sumTokens: List<String>,
        tokenVectorsMapList: MutableList<Map<String, INDArray>>,
    ) {
        val tempFile = File.createTempFile("word2vec_batch_$batchNumber", ".tmp")
        tempFile.deleteOnExit()

        tempFile.writeText(lines.joinToString("\n"))
        val wordVectors = WordVectorSerializer.readWord2VecModel(tempFile)

        // Consutruction de liste de map. pour chaque map la clé est le token et la valeur est le vecteur
        for (word in sumTokens) {
            if (wordVectors.hasWord(word)) {
                val tokenVectorMap = mapOf(word to wordVectors.getWordVectorMatrix(word))
                tokenVectorsMapList.add(tokenVectorMap)
            }
        }
    }

    private fun tokensToVector(
        sentenceTokens: List<String>,
        tokenVectorsMapList: MutableList<Map<String, INDArray>>,
    ): INDArray {
        val firstVectorMap = tokenVectorsMapList.first()
        val wordVectorSize = firstVectorMap.values.first().size(0)
        val sumVector = Nd4j.create(wordVectorSize)

        for (word in sentenceTokens) {
            for (tokenVectorMap in tokenVectorsMapList) {
                for ((token, vector) in tokenVectorMap) {
                    if (word == token) {
                        sumVector.addi(vector)
                    }
                }
            }
        }

        sumVector.divi(sentenceTokens.size.toDouble())
        return Transforms.unitVec(sumVector)
    }

    private fun cosineSimilarity(vector1: INDArray, vector2: INDArray): Double {
        return Transforms.cosineSim(vector1, vector2) * 100
    }
}

