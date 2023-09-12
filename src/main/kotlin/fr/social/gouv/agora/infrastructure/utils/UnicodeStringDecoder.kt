package fr.social.gouv.agora.infrastructure.utils

object UnicodeStringDecoder {

    fun decodeUnicode(string: String): String {
        var stringCopy: String = string.split(" ")[0]
        stringCopy = stringCopy.replace("\\", "")

        val stringSplit = stringCopy.split("u".toRegex()).toTypedArray()

        var decodedText = ""
        for (i in 1 until stringSplit.size) {
            val hexVal = stringSplit[i].toInt(16)
            decodedText += hexVal.toChar()
        }

        return decodedText
    }
}