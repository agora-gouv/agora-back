package fr.gouv.agora.usecase.themeHebdo

import fr.gouv.agora.domain.ThemeHebdo
import fr.gouv.agora.usecase.themeHebdo.repository.ThemeHebdoRepository
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.TextStyle
import java.util.Date
import java.util.Locale

@Service
class GetThemeHebdoUseCase(
    private val themeHebdoRepository: ThemeHebdoRepository,
    private val clock: Clock,
) {

    fun getThemeHebdo(): ThemeHebdo {
        val themeHebdoList = themeHebdoRepository.getThemeHebdoList()
        val current = themeHebdoList.firstOrNull { isCurrentDateInRange(it) } ?: ThemeHebdo()
        val result = getThemeHebdoWithDefaultDates(current).let { getThemeHebdoWithPeriode(it) }
        val prochainsThemes = getProchainsThemes(themeHebdoList, result)
        result.prochainsThemes = prochainsThemes
        if (result.estThemeLibre) {
            result.sousTitre = "Posez les questions qui vous tiennent à coeur. Tous les thèmes, tous les ministères"
        }
        result.periode = result.periode.uppercase()
        return result
    }

    private fun isCurrentDateInRange(themeHebdo: ThemeHebdo): Boolean {
        val now = Date(clock.millis())
        val debut = themeHebdo.dateDebutTheme ?: return false
        val fin = themeHebdo.dateFinTheme ?: return false
        return !now.before(debut) && !now.after(fin)
    }

    private fun getThemeHebdoWithDefaultDates(themeHebdo: ThemeHebdo): ThemeHebdo {
        val today = LocalDate.ofInstant(clock.instant(), ZoneOffset.UTC)
        val monday = today.with(DayOfWeek.MONDAY)
        val sunday = today.with(DayOfWeek.SUNDAY)

        val defaultDebut = Date.from(LocalDateTime.of(monday.year, monday.month, monday.dayOfMonth, 0, 5).toInstant(ZoneOffset.UTC))
        val defaultFin = Date.from(LocalDateTime.of(sunday.year, sunday.month, sunday.dayOfMonth, 23, 55).toInstant(ZoneOffset.UTC))

        return themeHebdo.copy(
            dateDebutTheme = themeHebdo.dateDebutTheme ?: defaultDebut,
            dateFinTheme = themeHebdo.dateFinTheme ?: defaultFin,
        )
    }

    private fun getThemeHebdoWithPeriode(themeHebdo: ThemeHebdo): ThemeHebdo {
        if (!themeHebdo.periode.isNullOrEmpty()) return themeHebdo
        val debut = themeHebdo.dateDebutTheme ?: return themeHebdo
        val fin = themeHebdo.dateFinTheme ?: return themeHebdo
        return themeHebdo.copy(periode = generatePeriodeFromDates(debut, fin))
    }

    private fun getProchainsThemes(allThemes: List<ThemeHebdo>, current: ThemeHebdo): List<String> {
        val currentFin = current.dateFinTheme ?: return emptyList()
        return allThemes
            .filter { it.dateDebutTheme != null && it.dateDebutTheme.after(currentFin) }
            .sortedBy { it.dateDebutTheme }
            .take(3)
            .map { it.theme }
    }

    private fun generatePeriodeFromDates(debut: Date, fin: Date): String {
        val debutLocal = debut.toInstant().atOffset(ZoneOffset.UTC).toLocalDate()
        val finLocal = fin.toInstant().atOffset(ZoneOffset.UTC).toLocalDate()
        val french = Locale.FRENCH
        return if (debutLocal.month == finLocal.month) {
            val monthName = debutLocal.month.getDisplayName(TextStyle.FULL, french)
            "${debutLocal.dayOfMonth}-${finLocal.dayOfMonth} $monthName"
        } else {
            val debutMonthName = debutLocal.month.getDisplayName(TextStyle.FULL, french)
            val finMonthName = finLocal.month.getDisplayName(TextStyle.FULL, french)
            "${debutLocal.dayOfMonth} $debutMonthName - ${finLocal.dayOfMonth} $finMonthName"
        }
    }
}
