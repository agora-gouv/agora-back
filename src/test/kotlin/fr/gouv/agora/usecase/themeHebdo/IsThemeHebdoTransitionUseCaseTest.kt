package fr.gouv.agora.usecase.themeHebdo

import fr.gouv.agora.domain.ThemeHebdo
import fr.gouv.agora.usecase.themeHebdo.repository.ThemeHebdoRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import java.util.Date

@ExtendWith(MockitoExtension::class)
internal class IsThemeHebdoTransitionUseCaseTest {

    private lateinit var useCase: IsThemeHebdoTransitionUseCase

    @Mock
    private lateinit var themeHebdoRepository: ThemeHebdoRepository

    // now = lundi 14 juillet 2026 à 10h00 UTC (heure typique du cron)
    private val nowInstant = Instant.parse("2026-07-14T10:00:00Z")

    private fun buildThemeHebdo(
        dateDebutTheme: Date? = null,
        dateFinTheme: Date? = null,
    ) = ThemeHebdo(
        dateDebutTheme = dateDebutTheme,
        dateFinTheme = dateFinTheme,
    )

    private fun mockClock(instant: Instant) {
        val clock = Clock.fixed(instant, ZoneOffset.UTC)
        useCase = IsThemeHebdoTransitionUseCase(
            themeHebdoRepository = themeHebdoRepository,
            clock = clock,
        )
    }

    @Nested
    inner class EmptyOrNullTests {

        @Test
        fun `isInTransition - when theme list is empty - should return false`() {
            // Given
            mockClock(nowInstant)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(emptyList())

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isFalse()
        }

        @Test
        fun `isInTransition - when theme has null dateDebutTheme and null dateFinTheme - should return false`() {
            // Given
            mockClock(nowInstant)
            val theme = buildThemeHebdo(dateDebutTheme = null, dateFinTheme = null)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(theme))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isFalse()
        }

        @Test
        fun `isInTransition - when theme has null dateDebutTheme but dateFinTheme is outside window - should return false`() {
            // Given
            mockClock(nowInstant)
            val dateFin = Date.from(nowInstant.plusSeconds(7 * 24 * 3600)) // dans 7 jours
            val theme = buildThemeHebdo(dateDebutTheme = null, dateFinTheme = dateFin)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(theme))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isFalse()
        }
    }

    @Nested
    inner class DateFinThemeTests {

        @Test
        fun `isInTransition - when now is exactly at dateFinTheme - should return true`() {
            // Given
            mockClock(nowInstant)
            val dateFin = Date.from(nowInstant)
            val theme = buildThemeHebdo(dateFinTheme = dateFin)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(theme))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isTrue()
        }

        @Test
        fun `isInTransition - when now is exactly 6h before dateFinTheme - should return true`() {
            // Given
            mockClock(nowInstant)
            val dateFin = Date.from(nowInstant.plusSeconds(6 * 3600)) // +6h
            val theme = buildThemeHebdo(dateFinTheme = dateFin)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(theme))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isTrue()
        }

        @Test
        fun `isInTransition - when now is exactly 6h after dateFinTheme - should return true`() {
            // Given
            mockClock(nowInstant)
            val dateFin = Date.from(nowInstant.minusSeconds(6 * 3600)) // -6h
            val theme = buildThemeHebdo(dateFinTheme = dateFin)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(theme))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isTrue()
        }

        @Test
        fun `isInTransition - when now is 6h01 before dateFinTheme - should return false`() {
            // Given
            mockClock(nowInstant)
            val dateFin = Date.from(nowInstant.plusSeconds(6 * 3600 + 60)) // +6h01
            val theme = buildThemeHebdo(dateFinTheme = dateFin)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(theme))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isFalse()
        }

        @Test
        fun `isInTransition - when now is 6h01 after dateFinTheme - should return false`() {
            // Given
            mockClock(nowInstant)
            val dateFin = Date.from(nowInstant.minusSeconds(6 * 3600 + 60)) // -6h01
            val theme = buildThemeHebdo(dateFinTheme = dateFin)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(theme))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isFalse()
        }
    }

    @Nested
    inner class DateDebutThemeTests {

        @Test
        fun `isInTransition - when now is exactly at dateDebutTheme - should return true`() {
            // Given
            mockClock(nowInstant)
            val dateDebut = Date.from(nowInstant)
            val theme = buildThemeHebdo(dateDebutTheme = dateDebut)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(theme))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isTrue()
        }

        @Test
        fun `isInTransition - when now is exactly 6h before dateDebutTheme - should return true`() {
            // Given
            mockClock(nowInstant)
            val dateDebut = Date.from(nowInstant.plusSeconds(6 * 3600)) // +6h
            val theme = buildThemeHebdo(dateDebutTheme = dateDebut)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(theme))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isTrue()
        }

        @Test
        fun `isInTransition - when now is exactly 6h after dateDebutTheme - should return true`() {
            // Given
            mockClock(nowInstant)
            val dateDebut = Date.from(nowInstant.minusSeconds(6 * 3600)) // -6h
            val theme = buildThemeHebdo(dateDebutTheme = dateDebut)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(theme))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isTrue()
        }

        @Test
        fun `isInTransition - when now is 6h01 before dateDebutTheme - should return false`() {
            // Given
            mockClock(nowInstant)
            val dateDebut = Date.from(nowInstant.plusSeconds(6 * 3600 + 60)) // +6h01
            val theme = buildThemeHebdo(dateDebutTheme = dateDebut)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(theme))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isFalse()
        }

        @Test
        fun `isInTransition - when now is 6h01 after dateDebutTheme - should return false`() {
            // Given
            mockClock(nowInstant)
            val dateDebut = Date.from(nowInstant.minusSeconds(6 * 3600 + 60)) // -6h01
            val theme = buildThemeHebdo(dateDebutTheme = dateDebut)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(theme))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isFalse()
        }
    }

    @Nested
    inner class MultipleThemesTests {

        @Test
        fun `isInTransition - when multiple themes exist and none is in transition window - should return false`() {
            // Given
            mockClock(nowInstant)
            val themePasse = buildThemeHebdo(
                dateDebutTheme = Date.from(nowInstant.minusSeconds(14 * 24 * 3600)),
                dateFinTheme = Date.from(nowInstant.minusSeconds(7 * 24 * 3600)),
            )
            val themeFutur = buildThemeHebdo(
                dateDebutTheme = Date.from(nowInstant.plusSeconds(7 * 24 * 3600)),
                dateFinTheme = Date.from(nowInstant.plusSeconds(14 * 24 * 3600)),
            )
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(themePasse, themeFutur))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isFalse()
        }

        @Test
        fun `isInTransition - when multiple themes exist and one has dateFinTheme within window - should return true`() {
            // Given
            mockClock(nowInstant)
            val themeCourant = buildThemeHebdo(
                dateDebutTheme = Date.from(nowInstant.minusSeconds(7 * 24 * 3600)),
                dateFinTheme = Date.from(nowInstant.minusSeconds(3600)), // fin il y a 1h, dans la fenêtre
            )
            val themeFutur = buildThemeHebdo(
                dateDebutTheme = Date.from(nowInstant.plusSeconds(7 * 24 * 3600)),
                dateFinTheme = Date.from(nowInstant.plusSeconds(14 * 24 * 3600)),
            )
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(themeCourant, themeFutur))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isTrue()
        }

        @Test
        fun `isInTransition - when multiple themes exist and one has dateDebutTheme within window - should return true`() {
            // Given
            mockClock(nowInstant)
            val themePasse = buildThemeHebdo(
                dateDebutTheme = Date.from(nowInstant.minusSeconds(14 * 24 * 3600)),
                dateFinTheme = Date.from(nowInstant.minusSeconds(7 * 24 * 3600)),
            )
            val themeNouveau = buildThemeHebdo(
                dateDebutTheme = Date.from(nowInstant.plusSeconds(3600)), // début dans 1h, dans la fenêtre
                dateFinTheme = Date.from(nowInstant.plusSeconds(7 * 24 * 3600)),
            )
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(listOf(themePasse, themeNouveau))

            // When
            val result = useCase.isInTransition()

            // Then
            assertThat(result).isTrue()
        }

        @Test
        fun `isInTransition - when repository is called only once`() {
            // Given
            mockClock(nowInstant)
            given(themeHebdoRepository.getThemeHebdoList()).willReturn(emptyList())

            // When
            useCase.isInTransition()

            // Then
            then(themeHebdoRepository).should().getThemeHebdoList()
            then(themeHebdoRepository).shouldHaveNoMoreInteractions()
        }
    }
}
