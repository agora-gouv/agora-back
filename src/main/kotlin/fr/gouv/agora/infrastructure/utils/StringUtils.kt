package fr.gouv.agora.infrastructure.utils

import java.text.Normalizer

object StringUtils {

    private val REGEX_REMOVE_DIACRITICS = "\\p{InCombiningDiacriticalMarks}+".toRegex()

    fun unescapeLineBreaks(string: String): String {
        return string.replace("\\n", "\n")
    }

    fun replaceDiacritics(string: String): String {
        return REGEX_REMOVE_DIACRITICS.replace(Normalizer.normalize(string, Normalizer.Form.NFD), "")
    }

}