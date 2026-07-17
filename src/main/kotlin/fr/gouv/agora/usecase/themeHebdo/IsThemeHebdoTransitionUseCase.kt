package fr.gouv.agora.usecase.themeHebdo

import fr.gouv.agora.usecase.themeHebdo.repository.ThemeHebdoRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.Clock
import java.util.Date

@Service
class IsThemeHebdoTransitionUseCase(
    private val themeHebdoRepository: ThemeHebdoRepository,
    private val clock: Clock,
) {
    private val logger: Logger = LoggerFactory.getLogger(IsThemeHebdoTransitionUseCase::class.java)

    companion object {
        private const val TRANSITION_WINDOW_MS = 6 * 3_600_000L // 6 heures en millisecondes
    }

    fun isInTransition(): Boolean {
        val now = Date(clock.millis())
        val themes = themeHebdoRepository.getThemeHebdoList()

        val isTransition = themes.any { theme ->
            isWithinWindow(now, theme.dateDebutTheme) || isWithinWindow(now, theme.dateFinTheme)
        }

        if (isTransition) {
            logger.info("🗓️ Transition de thème hebdomadaire détectée (fenêtre de 6h)")
        } else {
            logger.info("🗓️ Aucune transition de thème hebdomadaire détectée")
        }

        return isTransition
    }

    private fun isWithinWindow(now: Date, boundary: Date?): Boolean {
        boundary ?: return false
        val diffMs = Math.abs(now.time - boundary.time)
        return diffMs <= TRANSITION_WINDOW_MS
    }
}
