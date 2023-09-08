package fr.social.gouv.agora.oninit

import fr.social.gouv.agora.domain.QagStatus
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

@Component
@Suppress("unused")
class WordVectorFileInitializer(
    private val qagInfoRepository: QagInfoRepository,
    private val sentenceToWordsGenerator: SentenceToWordsGenerator,
    private val vectorizedWordsRepository: VectorizedWordsRepository,
) : InitializingBean {

    companion object {
        private const val BASE_DOWNLOAD_URL = "https://betagouv.github.io/agora-content/wordVectorFiles/"
        private const val DOWNLOAD_DESTINATION_PATH = "/tmp/wordVectorFiles/"

        private const val WORD_VECTOR_FILE_PREFIX = "wordVectors_"
        private const val WORD_VECTOR_FILE_SUFFIX = ".gz"
        private const val WORD_VECTOR_FILE_INDEXED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789"

        private const val WORD_VECTOR_FILE_SPECIAL_CHARACTERS = "specials"
    }

    override fun afterPropertiesSet() {
        println("📚 Downloading word vector archives...")
        downloadAndExtractWordVectors()
        println("📚 Initializing current QaG words vector cache...")
        initializeCurrentQagWordsCache()
        println("📚 Word vectors initialization finished !")
    }

    private fun downloadAndExtractWordVectors() {
        val wordVectorDirectory = File(DOWNLOAD_DESTINATION_PATH)
        if (!wordVectorDirectory.exists()) {
            wordVectorDirectory.mkdirs()
        }

        (WORD_VECTOR_FILE_INDEXED_CHARACTERS.map { it.toString() } + WORD_VECTOR_FILE_SPECIAL_CHARACTERS).forEach { filePointer ->
            downloadWordVectorArchiveIfRequired(WORD_VECTOR_FILE_PREFIX + filePointer + WORD_VECTOR_FILE_SUFFIX)
        }
    }

    private fun downloadWordVectorArchiveIfRequired(archiveName: String) {
        val targetFile = File(DOWNLOAD_DESTINATION_PATH + archiveName)
        if (!targetFile.exists()) {
            println("📚 Downloading word vector archive $archiveName...")
            URL(BASE_DOWNLOAD_URL + archiveName).openStream().use { downloadStream ->
                Files.copy(downloadStream, Paths.get(DOWNLOAD_DESTINATION_PATH + archiveName))
            }
            println("📚 Extracting word vector archive $archiveName...")
            unzipIfRequired(archiveName)
        }
    }

    @Throws(ArchiverException::class)
    private fun unzipIfRequired(archiveName: String): List<File> {
        val archiveDirectory = File(DOWNLOAD_DESTINATION_PATH + archiveName)
        if (!archiveDirectory.exists()) {
            archiveDirectory.mkdirs()
            unzip(archiveDirectory)
        }

        return archiveDirectory.listFiles()?.sorted()?.toList() ?: emptyList()
    }

    @Throws(ArchiverException::class)
    private fun unzip(archiveDirectory: File) {
        val zippedFile = File(DOWNLOAD_DESTINATION_PATH + archiveDirectory.name + WORD_VECTOR_FILE_SUFFIX)

        val gzipUnArchiver = TarGZipUnArchiver()
        gzipUnArchiver.sourceFile = zippedFile
        gzipUnArchiver.destDirectory = archiveDirectory.parentFile
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
    }

}