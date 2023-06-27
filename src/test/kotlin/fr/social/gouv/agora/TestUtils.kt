package fr.social.gouv.agora

import fr.social.gouv.agora.infrastructure.utils.DateUtils.toDate
import java.time.Clock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId

object TestUtils {

    fun getFixedClock(dateTime: LocalDateTime): Clock {
        return Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())
    }

    fun getFixedClock(date: LocalDate): Clock {
        return Clock.fixed(date.toDate().toInstant(), ZoneId.systemDefault())
    }

}