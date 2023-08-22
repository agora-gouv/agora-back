package fr.social.gouv.agora.oninit

import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths

@Component
@Suppress("unused")
class WordVectorFileDownloader : InitializingBean {

    companion object {
        private const val BASE_DOWNLOAD_URL = "https://betagouv.github.io/agora-content/wordVectorFiles/"
        private const val DOWNLOAD_DESTINATION_PATH = "/tmp/wordVectorFiles/"

        private const val WORD_VECTOR_FILE_PREFIX = "wordVectors_"
        private const val WORD_VECTOR_FILE_SUFFIX = ".gz"
        private const val WORD_VECTOR_FILE_INDEXED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789"

        private const val WORD_VECTOR_FILE_SPECIAL_CHARACTERS = "specials"
    }

    override fun afterPropertiesSet() {
        (WORD_VECTOR_FILE_INDEXED_CHARACTERS.map { it.toString() } + WORD_VECTOR_FILE_SPECIAL_CHARACTERS).forEach { filePointer ->
            downloadWordVectorArchiveIfRequired(WORD_VECTOR_FILE_PREFIX + filePointer + WORD_VECTOR_FILE_SUFFIX)
        }
        println("Word vector archives download finished !")
    }

    private fun downloadWordVectorArchiveIfRequired(archiveName: String) {
        val targetFile = File(DOWNLOAD_DESTINATION_PATH + archiveName)
        if (!targetFile.exists()) {
            println("Downloading word vector archive $archiveName...")
            URL(BASE_DOWNLOAD_URL + archiveName).openStream().use { downloadStream ->
                Files.copy(downloadStream, Paths.get(DOWNLOAD_DESTINATION_PATH + archiveName))
            }
        }
    }

}