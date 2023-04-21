package fr.social.gouv.agora.infrastructure.utils

import java.util.*

object UuidUtils {

    const val NOT_FOUND_UUID_STRING = "00000000-0000-0000-0000-000000000000"
    val NOT_FOUND_UUID: UUID = UUID.fromString(NOT_FOUND_UUID_STRING)

}