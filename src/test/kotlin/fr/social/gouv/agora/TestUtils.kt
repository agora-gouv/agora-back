package fr.social.gouv.agora

import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId

object TestUtils {

    fun getFixedClock(dateTime: LocalDateTime): Clock {
        return Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())
    }

}