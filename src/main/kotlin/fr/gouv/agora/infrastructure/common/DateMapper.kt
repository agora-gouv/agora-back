package fr.gouv.agora.infrastructure.common

import fr.gouv.agora.infrastructure.utils.DateUtils.toLocalDateTime
import org.springframework.stereotype.Component
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.*

@Component
class DateMapper {

    companion object {
        private val DEFAULT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withLocale(Locale.FRANCE)
    }

    fun toFormattedDate(date: Date): String = DEFAULT_FORMAT.format(date.toLocalDateTime())

    fun toLocalDate(dateString: String): LocalDate? {
        return try {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            LocalDate.parse(dateString, formatter)
        } catch (e: DateTimeParseException) {
            null
        }
    }

    fun toLocalDateTime(dateTimeString: String): LocalDateTime? {
        return try {
            LocalDateTime.parse(dateTimeString, DateTimeFormatter.ISO_DATE_TIME)
        } catch (e: DateTimeParseException) {
            null
        }
    }
}