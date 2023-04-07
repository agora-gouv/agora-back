package fr.social.gouv.agora.infrastructure.utils

import java.util.*

object Base64ImageEncoder {

    fun toBase64(imageFilepath: String): String? {
        return this::class.java.getResource(imageFilepath)?.readBytes()?.let { imgBytes ->
            Base64.getEncoder().encodeToString(imgBytes)
        }
    }

}