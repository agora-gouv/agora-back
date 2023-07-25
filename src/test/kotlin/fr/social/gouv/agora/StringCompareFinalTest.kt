package fr.social.gouv.agora

import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import org.nd4j.linalg.ops.transforms.Transforms
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit.jupiter.SpringExtension
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.util.zip.GZIPInputStream

@ExtendWith(SpringExtension::class)
@SpringBootTest
class StringCompareFinalTest {

    @Test

    fun `test two strings`() {

        val tokenizerFactory = DefaultTokenizerFactory()
        val modelFilePath = "cc.fr.300.vec.gz"
        val batchSize = 1000

        val sentence1 = "bonjour vous vous appelez comment"
        val sentence2 = "bonjour vous vous appelez sara"
        val sentence1Tokens = sentenceToTokens(sentence1, tokenizerFactory)
        val sentence2Tokens = sentenceToTokens(sentence2, tokenizerFactory)
        val sumTokens = (sentence1Tokens + sentence2Tokens).distinct()

        val tokenVectorsMapList = mutableListOf<Map<String, INDArray>>()

        GZIPInputStream(this.javaClass.classLoader.getResourceAsStream(modelFilePath)).use { gzipInputStream ->
            val reader = InputStreamReader(gzipInputStream, "UTF-8")
            val bufferedReader = BufferedReader(reader)

            var fileCounter = 0
            val lines = mutableListOf<String>()

            bufferedReader.forEachLine loop@{ line ->
                if (tokenVectorsMapList.size < sumTokens.size) {
                    lines.add(line)
                    if (lines.size == batchSize) {
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
        println("similarité entre les deux phrases est: ${cosineSimilarity(sentence1Vector, sentence2Vector)}")
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
