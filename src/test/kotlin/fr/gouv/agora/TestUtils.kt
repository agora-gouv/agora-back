package fr.gouv.agora

import org.mockito.BDDMockito.lenient
import org.mockito.stubbing.OngoingStubbing
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId

object TestUtils {

    fun getFixedClock(dateTime: LocalDateTime): Clock {
        return Clock.fixed(dateTime.atZone(ZoneId.systemDefault()).toInstant(), ZoneId.systemDefault())
    }

    fun <T> lenientGiven(methodCall: T): OngoingStubbing<T?> {
        return lenient().`when`(methodCall)
    }

    fun <T> OngoingStubbing<T>.willReturn(value: T): OngoingStubbing<T?> {
        return this.thenReturn(value)
    }

}