package fr.social.gouv.agora

import org.deeplearning4j.text.sentenceiterator.SentencePreProcessor
import java.util.regex.Pattern

class SentenceNormalizer : SentencePreProcessor {

    companion object {
        private val PUNCTUATION_CHARS_PATTERN = Pattern.compile("[\\d:,\"\'`_|?!\n\r@;.()\\[]\\{}+")
        private val COMMON_WORDS_TO_REMOVE =
            listOf(
                "le",
                "la",
                "là",
                "les",
                "de",
                "des",
                "à",
                "et",
                "par",
                "où",
                "mais",
                "donc",
                "car",
                "or",
                "ni",
                "au",
                "y",
                "pour",
                "dans",
                "en",
                "quel",
                "quelle",
                "quels",
                "quelles",
                "je",
                "tu",
                "elle",
                "il",
                "on",
                "nous",
                "vous",
                "elles",
                "ils"
            )
    }

    override fun preProcess(sentence: String): String {
        return PUNCTUATION_CHARS_PATTERN.matcher(sentence.trim().lowercase()).replaceAll("")
            .replace("\\{.*?}".toRegex(), "")
            .replace("\\[.*?]".toRegex(), "")
            .replace("\\(.*?\\)".toRegex(), "")
            .replace("[/]".toRegex(), " ")
            .replace(";".toRegex(), " ")
            .split(" ").filterNot { COMMON_WORDS_TO_REMOVE.contains(it) }
            .joinToString(" ")
    }

}