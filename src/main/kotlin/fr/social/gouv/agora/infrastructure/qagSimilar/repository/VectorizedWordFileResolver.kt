package fr.social.gouv.agora.infrastructure.qagSimilar.repository

import org.springframework.stereotype.Component

@Component
class VectorizedWordFileResolver {

    companion object {
        private const val INDEXED_CHARACTERS = "abcdefghijklmnopqrstuvwxyz0123456789"
        private const val WORD_VECTOR_FILE_PREFIX = "wordVectors_"
        private const val WORD_VECTOR_FILE_SPECIAL_CHARACTERS = "specials"
    }

    fun findWordArchiveName(word: String): String {
        val firstCharacter = word.lowercase()[0]

        val filePointer = if (INDEXED_CHARACTERS.contains(firstCharacter)) {
            firstCharacter
        } else {
            WORD_VECTOR_FILE_SPECIAL_CHARACTERS
        }

        return WORD_VECTOR_FILE_PREFIX + filePointer
    }

}