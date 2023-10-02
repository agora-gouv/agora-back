package fr.social.gouv.agora.infrastructure.utils

import java.util.*

object UuidUtils {

    const val NOT_FOUND_UUID_STRING = "00000000-0000-0000-0000-000000000000"
    val NOT_FOUND_UUID: UUID = UUID.fromString(NOT_FOUND_UUID_STRING)
    val DELETED_UUID: UUID = UUID.fromString(NOT_FOUND_UUID_STRING)

    const val SKIP_QUESTION_CHOICE_UUID = NOT_FOUND_UUID_STRING
    const val NOT_APPLICABLE_CHOICE_UUID = "11111111-1111-1111-1111-111111111111"

    fun String.toUuidOrNull() = try {
        UUID.fromString(this)
    } catch (e: IllegalArgumentException) {
        null
    }

}