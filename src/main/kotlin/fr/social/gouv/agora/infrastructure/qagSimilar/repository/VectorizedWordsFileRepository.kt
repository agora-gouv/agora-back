package fr.social.gouv.agora.infrastructure.qagSimilar.repository

//@Repository
//class VectorizedWordsFileRepository(private val resolver: VectorizedWordFileResolver) {
//
//    companion object {
//        private const val ZIP_FILE_PREFIX = "wordVectorFiles"
//        private const val UNZIPPED_WORD_VECTOR_DIRECTORY = "/tmp/$ZIP_FILE_PREFIX/"
//    }
//
//    fun getWordVectors(words: List<String>): Map<String, VectorResult> {
//        val archivesToWords = words.map { word -> resolver.findWordArchiveName(word) to word }
//            .groupBy { it.first }
//            .mapValues { (_, words) ->
//                words.map { archiveToWords -> archiveToWords.second }
//            }
//            .toList()
//
//        return archivesToWords.fold(initial = emptyMap()) { initial, (archiveName, words) ->
//            initial + getVectorsFromArchive(archiveName, words)
//        }
//    }
//
//    private fun getVectorsFromArchive(archiveName: String, words: List<String>): Map<String, VectorResult> {
//        println("📚 Looking in $archiveName for words= $words")
//        val wordVectorMap = mutableMapOf<String, VectorResult>()
//        getArchiveFiles(archiveName)?.forEach { wordVectorFile ->
//            val wordsFromVectorFile = searchWordsInVectorFile(wordVectorFile, words)
//            wordVectorMap.putAll(wordsFromVectorFile)
//            if (wordVectorMap.size == words.size) {
//                return wordVectorMap
//            }
//        } ?: return words.associateWith { VectorResult.VectorError }
//
//        val missingWordVectors = words.filterNot { word -> wordVectorMap.containsKey(word) }
//        return wordVectorMap + missingWordVectors.map { word -> word to VectorResult.VectorFound(vector = null) }
//    }
//
//    private fun getArchiveFiles(archiveName: String): List<File>? {
//        val archiveDirectory = File(UNZIPPED_WORD_VECTOR_DIRECTORY + archiveName)
//        if (!archiveDirectory.exists()) {
//            return null
//        }
//
//        return archiveDirectory.listFiles()?.sorted()?.toList() ?: emptyList()
//    }
//
//    private fun searchWordsInVectorFile(
//        wordVectorFile: File,
//        words: List<String>,
//    ): Map<String, VectorResult> {
//        val word2Vec = WordVectorSerializer.readWord2VecModel(wordVectorFile)
//        return words
//            .map { word -> word to word2Vec.getWordVectorMatrix(word) }
//            .filter { (_, vector) -> vector != null }
//            .associate { (word, vector) ->
//                println("📚 Word vector found : $word")
//                word to VectorResult.VectorFound(vector)
//            }
//    }
//}

//sealed class VectorResult {
//    data class VectorFound(val vector: INDArray?) : VectorResult()
//    object VectorError : VectorResult()
//}