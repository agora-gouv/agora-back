package fr.gouv.agora.infrastructure.utils

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.util.*

object DateUtils {

    fun LocalDateTime.toDate(): Date {
        return Date.from(this.atZone(ZoneId.systemDefault()).toInstant())
    }

    fun LocalDate.toDate(): Date {
        val startOfDay = this.atStartOfDay()
        val currentTime = LocalTime.now(ZoneId.systemDefault())
        return Date.from(startOfDay.with(currentTime).atZone(ZoneId.systemDefault()).toInstant())
    }

    fun Date.toLocalDate(): LocalDate {
        return LocalDate.ofInstant(this.toInstant(), ZoneId.systemDefault())
    }

    fun Date.toLocalDateTime(): LocalDateTime {
        return LocalDateTime.ofInstant(this.toInstant(), ZoneId.systemDefault())
    }

}