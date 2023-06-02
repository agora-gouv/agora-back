package fr.social.gouv.agora.infrastructure.utils

import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

object DateUtils {

    fun LocalDateTime.toDate(): Date {
        return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
    }

}