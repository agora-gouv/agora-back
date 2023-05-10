package fr.social.gouv.agora.infrastructure.utils

object StringUtils {

    fun unescapeLineBreaks(string: String): String {
        return string.replace("\\n", "\n")
    }

}