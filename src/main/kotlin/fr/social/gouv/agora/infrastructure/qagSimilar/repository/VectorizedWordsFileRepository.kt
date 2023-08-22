package fr.social.gouv.agora.infrastructure.qagSimilar.repository

import org.codehaus.plexus.archiver.ArchiverException
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.nd4j.linalg.api.ndarray.INDArray
import org.springframework.stereotype.Repository
import java.io.File


@Repository
class VectorizedWordsFileRepository(private val resolver: VectorizedWordFileResolver) {

    companion object {
        private const val ZIP_FILE_PREFIX = "wordVectorFiles"
        private const val ZIP_FILE_SUFFIX = ".gz"

        private const val ZIPPED_WORD_VECTOR_DIRECTORY = "/tmp/wordVectorFiles/"
        private const val UNZIPPED_WORD_VECTOR_DIRECTORY = "/tmp/$ZIP_FILE_PREFIX/"
    }

    fun getWordVectors(words: List<String>): Map<String, VectorResult> {
        val archivesToWords = words.map { word -> resolver.findWordArchiveName(word) to word }
            .groupBy { it.first }
            .mapValues { (_, words) ->
                words.map { archiveToWords -> archiveToWords.second }
            }
            .toList()

        return archivesToWords.fold(initial = emptyMap()) { initial, (archiveName, words) ->
            initial + getVectorsFromArchive(archiveName, words)
        }
    }

    private fun getVectorsFromArchive(archiveName: String, words: List<String>): Map<String, VectorResult> {
        try {
            val wordVectorMap = mutableMapOf<String, VectorResult>()
            unzipIfRequired(archiveName).forEach { wordVectorFile ->
                val wordsFromVectorFile = searchWordsInVectorFile(wordVectorFile, words)
                wordVectorMap.putAll(wordsFromVectorFile)
                if (wordVectorMap.size == words.size) {
                    return wordVectorMap
                }
            }

            val missingWordVectors = words.filterNot { word -> wordVectorMap.containsKey(word) }
            return wordVectorMap + missingWordVectors.map { word -> word to VectorResult.VectorFound(vector = null) }
        } catch (e: ArchiverException) {
            return words.associateWith { VectorResult.VectorError }
        }
    }

    @Throws(ArchiverException::class)
    private fun unzipIfRequired(archiveName: String): List<File> {
        val archiveDirectory = File(UNZIPPED_WORD_VECTOR_DIRECTORY + archiveName)
        if (!archiveDirectory.exists()) {
            archiveDirectory.mkdirs()
            unzip(archiveDirectory)
        }

        return archiveDirectory.listFiles()?.sorted()?.toList() ?: emptyList()
    }

    @Throws(ArchiverException::class)
    private fun unzip(archiveDirectory: File) {
        val zippedFile = File(ZIPPED_WORD_VECTOR_DIRECTORY + archiveDirectory.name + ZIP_FILE_SUFFIX)

        val gzipUnArchiver = TarGZipUnArchiver()
        gzipUnArchiver.sourceFile = zippedFile
        gzipUnArchiver.destDirectory = archiveDirectory.parentFile
        gzipUnArchiver.extract()
    }

    private fun searchWordsInVectorFile(
        wordVectorFile: File,
        words: List<String>,
    ): Map<String, VectorResult> {
        val word2Vec = WordVectorSerializer.readWord2VecModel(wordVectorFile)
        return words
            .map { word -> word to word2Vec.getWordVectorMatrix(word) }
            .filter { (_, vector) -> vector != null }
            .associate { (word, vector) -> word to VectorResult.VectorFound(vector) }
    }
}

sealed class VectorResult {
    data class VectorFound(val vector: INDArray?) : VectorResult()
    object VectorError : VectorResult()
}