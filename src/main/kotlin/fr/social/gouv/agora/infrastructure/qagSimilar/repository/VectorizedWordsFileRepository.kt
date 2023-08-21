package fr.social.gouv.agora.infrastructure.qagSimilar.repository

import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver
import org.deeplearning4j.models.embeddings.loader.WordVectorSerializer
import org.nd4j.linalg.api.ndarray.INDArray
import org.springframework.stereotype.Repository
import java.io.File
import java.net.URLDecoder
import java.nio.charset.Charset


@Repository
class VectorizedWordsFileRepository(private val resolver: VectorizedWordFileResolver) {

    companion object {
        private const val ZIP_FILE_PREFIX = "wordVectorFiles"
        private const val ZIP_FILE_SUFFIX = ".gz"

        private const val UNZIPPED_WORD_VECTOR_DIRECTORY = "/tmp/$ZIP_FILE_PREFIX/"
    }

    fun getWordVectors(words: List<String>): Map<String, INDArray?> {
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

    private fun getVectorsFromArchive(archiveName: String, words: List<String>): Map<String, INDArray?> {
        val wordVectorMap = mutableMapOf<String, INDArray?>()
        unzipIfRequired(archiveName).forEach { wordVectorFile ->
            println("Read wordVector file ${wordVectorFile.name}")
            val word2Vec = WordVectorSerializer.readWord2VecModel(wordVectorFile)

            val wordVectors = words.map { word ->
                word to word2Vec.getWordVectorMatrix(word)
            }.filter { it.second != null }
            wordVectorMap.putAll(wordVectors)

            if (wordVectorMap.size == words.size) {
                return wordVectorMap
            }
        }

        val missingWordVectors = words.filterNot { word -> wordVectorMap.containsKey(word) }
        return wordVectorMap + missingWordVectors.map { word -> word to null }
    }

    private fun unzipIfRequired(archiveName: String): List<File> {
        val archiveDirectory = File(UNZIPPED_WORD_VECTOR_DIRECTORY + archiveName)
        if (!archiveDirectory.exists()) {
            archiveDirectory.mkdirs()
            unzip(archiveDirectory)
        }

        return archiveDirectory.listFiles()?.toList() ?: emptyList()
    }

    private fun unzip(archiveDirectory: File) {
        val zippedFileName = ZIP_FILE_PREFIX + "/" + archiveDirectory.name + ZIP_FILE_SUFFIX
        val zippedFile = File(
            URLDecoder.decode(
                this.javaClass.classLoader.getResource(zippedFileName)?.file,
                Charset.defaultCharset()
            )
        )

        val gzipUnArchiver = TarGZipUnArchiver()
        gzipUnArchiver.sourceFile = zippedFile
        gzipUnArchiver.destDirectory = archiveDirectory.parentFile
        gzipUnArchiver.extract()
    }
}