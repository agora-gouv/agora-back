package fr.social.gouv.agora.oninit

import fr.social.gouv.agora.domain.AgoraFeature
import fr.social.gouv.agora.domain.QagStatus
import fr.social.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.social.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.social.gouv.agora.usecase.qagSimilar.SentenceToWordsGenerator
import fr.social.gouv.agora.usecase.qagSimilar.repository.VectorizedWordsRepository
import org.codehaus.plexus.archiver.ArchiverException
import org.codehaus.plexus.archiver.tar.TarGZipUnArchiver
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.CompletableFuture

@Component
@Suppress("unused")
class WordVectorFileInitializer(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val sentenceToWordsGenerator: SentenceToWordsGenerator,
    private val vectorizedWordsRepository: VectorizedWordsRepository,
) : InitializingBean {

    companion object {
        private const val BASE_DOWNLOAD_URL = "https://betagouv.github.io/agora-content/wordVectorFiles/"
        private const val DOWNLOAD_DESTINATION_PATH = "/tmp/wordVectorFiles/"

        private const val WORD_VECTOR_FILE_PREFIX = "wordVectors_"
        private const val WORD_VECTOR_ZIPPED_FILE_SUFFIX = ".gz"
        private const val WORD_VECTOR_FILE_INDEXED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789"

        private const val WORD_VECTOR_FILE_SPECIAL_CHARACTERS = "specials"
    }

    override fun afterPropertiesSet() {
        if (featureFlagsRepository.isFeatureEnabled(AgoraFeature.SimilarQag)) {
            println("📚 Downloading word vector archives...")
            CompletableFuture.supplyAsync {
                downloadAndExtractWordVectors()
                println("📚 Initializing current QaG words vector cache...")
                initializeCurrentQagWordsCache()
            }.thenAccept {
                println("📚 Word vectors initialization finished !")
            }
        }
    }

    private fun downloadAndExtractWordVectors() {
        val wordVectorDirectory = File(DOWNLOAD_DESTINATION_PATH)
        if (!wordVectorDirectory.exists()) {
            wordVectorDirectory.mkdirs()
        }

        (WORD_VECTOR_FILE_INDEXED_CHARACTERS.map { it.toString() } + WORD_VECTOR_FILE_SPECIAL_CHARACTERS).forEach { filePointer ->
            val archiveName = WORD_VECTOR_FILE_PREFIX + filePointer
            downloadWordVectorArchiveIfRequired(archiveName)
        }
    }

    private fun downloadWordVectorArchiveIfRequired(archiveName: String) {
        val extractedFile = getExtractedFile(archiveName)
        if (!extractedFile.exists()) {
            println("📚 Downloading word vector archive $archiveName...")
            val zippedFile = getZippedFile(archiveName)
            URL(BASE_DOWNLOAD_URL + archiveName + WORD_VECTOR_ZIPPED_FILE_SUFFIX).openStream().use { downloadStream ->
                Files.copy(downloadStream, Paths.get(zippedFile.absolutePath))
            }
            println("📚 Extracting word vector archive $archiveName...")
            unzipIfRequired(zippedFile, extractedFile)
            zippedFile.delete()
        }
    }

    @Throws(ArchiverException::class)
    private fun unzipIfRequired(zippedFile: File, extractedFile: File): List<File> {
        if (!extractedFile.exists()) {
            extractedFile.mkdirs()
            unzip(zippedFile, extractedFile)
        }

        return extractedFile.listFiles()?.sorted()?.toList() ?: emptyList()
    }

    @Throws(ArchiverException::class)
    private fun unzip(zippedFile: File, extractedFile: File) {
        val gzipUnArchiver = TarGZipUnArchiver()
        gzipUnArchiver.sourceFile = zippedFile
        gzipUnArchiver.destDirectory = extractedFile.parentFile
        gzipUnArchiver.extract()
    }

    private fun initializeCurrentQagWordsCache() {
        val currentQagWords = qagInfoRepository
            .getAllQagInfo()
            .filter { qagInfo -> qagInfo.status == QagStatus.MODERATED_ACCEPTED }
            .flatMap { qagInfo -> sentenceToWordsGenerator.sentenceToWords(qagInfo.title) }
            .distinct()
        println("📚 Retrieving vectors for words... : $currentQagWords")
        vectorizedWordsRepository.getWordVectors(currentQagWords)
        println("📚 Retrieving vectors finished !")
    }

    private fun getExtractedFile(archiveName: String) = File(DOWNLOAD_DESTINATION_PATH + archiveName)
    private fun getZippedFile(archiveName: String) =
        File(getExtractedFile(archiveName).absolutePath + WORD_VECTOR_ZIPPED_FILE_SUFFIX)

}