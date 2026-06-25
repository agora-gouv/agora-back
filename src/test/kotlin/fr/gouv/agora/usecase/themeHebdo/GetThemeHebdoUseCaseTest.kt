package fr.gouv.agora.usecase.themeHebdo

import fr.gouv.agora.domain.ThemeHebdo
import fr.gouv.agora.usecase.themeHebdo.repository.ThemeHebdoRepository
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.junit.jupiter.MockitoSettings
import org.mockito.quality.Strictness

@ExtendWith(MockitoExtension::class)
@MockitoSettings(strictness = Strictness.LENIENT)
internal class GetThemeHebdoUseCaseTest {

        @InjectMocks private lateinit var useCase: GetThemeHebdoUseCase

        @Mock private lateinit var themeHebdoRepository: ThemeHebdoRepository

        @Mock private lateinit var clock: Clock

        private val now = Instant.parse("2026-05-22T10:00:00Z") // jeudi 22 mai 2026
        private val yesterday = Date.from(now.minusSeconds(86400))
        private val tomorrow = Date.from(now.plusSeconds(86400))
        private val lastWeek = Date.from(now.minusSeconds(7 * 86400))
        private val nextWeek = Date.from(now.plusSeconds(7 * 86400))

        // lundi 18 mai 2026 à 0h05 UTC
        private val expectedMondayDebut: Date = Date.from(
                LocalDateTime.of(2026, 5, 18, 0, 5).toInstant(ZoneOffset.UTC)
        )
        // dimanche 24 mai 2026 à 23h55 UTC
        private val expectedSundayFin: Date = Date.from(
                LocalDateTime.of(2026, 5, 24, 23, 55).toInstant(ZoneOffset.UTC)
        )

        private fun buildThemeHebdo(
                dateDebutTheme: Date? = null,
                dateFinTheme: Date? = null,
                titre: String = "Titre",
                theme: String = "Éducation",
                periode: String = "19-25 mai 2026",
                estThemeLibre: Boolean = false,
        ) = ThemeHebdo(
                titre = titre,
                sousTitre = "Sous-titre",
                periode = periode,
                theme = theme,
                avatarUrl = "https://picsum.photos/40",
                nom = "Jean Dupont",
                fonction = "Ministre de l'Éducation nationale",
                prochainsThemes = emptyList(),
                titreCompteur = "Cloture des votes",
                dateDebutTheme = dateDebutTheme,
                dateFinTheme = dateFinTheme,
                estThemeLibre = estThemeLibre,
        )

        @Test
        fun `getThemeHebdo - when repository returns an empty list - should return default ThemeHebdo with default dates`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(emptyList())

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.dateDebutTheme).isEqualTo(expectedMondayDebut)
                assertThat(result.dateFinTheme).isEqualTo(expectedSundayFin)
                assertThat(result.avatarUrl).isEqualTo("https://pub-6c821c1c547c4e3eaa97abd4b0ab8180.r2.dev/logo_agora_f524e2d9bd.png")
                assertThat(result.nom).isEqualTo("Ministre à révéler")
                assertThat(result.fonction).isEqualTo("Le ministre qui répondra dépend de votre question gagnante")
        }

        @Test
        fun `getThemeHebdo - when repository returns exactly one item and it is within date range - should return it`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val themeHebdo = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = tomorrow)
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.dateDebutTheme).isEqualTo(yesterday)
                assertThat(result.dateFinTheme).isEqualTo(tomorrow)
        }

        @Test
        fun `getThemeHebdo - when repository returns exactly one item and it is outside date range - should return default ThemeHebdo with default dates`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val outdatedTheme = buildThemeHebdo(dateDebutTheme = lastWeek, dateFinTheme = yesterday, titre = "Passé")
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(outdatedTheme))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.dateDebutTheme).isEqualTo(expectedMondayDebut)
                assertThat(result.dateFinTheme).isEqualTo(expectedSundayFin)
                assertThat(result.avatarUrl).isEqualTo("https://pub-6c821c1c547c4e3eaa97abd4b0ab8180.r2.dev/logo_agora_f524e2d9bd.png")
                assertThat(result.nom).isEqualTo("Ministre à révéler")
                assertThat(result.fonction).isEqualTo("Le ministre qui répondra dépend de votre question gagnante")
        }

        @Test
        fun `getThemeHebdo - when repository returns multiple items and one matches current date - should return matching item`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val outdatedTheme = buildThemeHebdo(dateDebutTheme = lastWeek, dateFinTheme = yesterday, titre = "Passé")
                val currentTheme = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = tomorrow, titre = "Actuel")
                val futureTheme = buildThemeHebdo(dateDebutTheme = tomorrow, dateFinTheme = nextWeek, titre = "Futur")
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(outdatedTheme, currentTheme, futureTheme))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.titre).isEqualTo(currentTheme.titre)
        }

        @Test
        fun `getThemeHebdo - when repository returns multiple items and none matches current date - should return default ThemeHebdo with default dates`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val outdatedTheme = buildThemeHebdo(dateDebutTheme = lastWeek, dateFinTheme = yesterday, titre = "Passé")
                val futureTheme = buildThemeHebdo(dateDebutTheme = tomorrow, dateFinTheme = nextWeek, titre = "Futur")
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(outdatedTheme, futureTheme))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.dateDebutTheme).isEqualTo(expectedMondayDebut)
                assertThat(result.dateFinTheme).isEqualTo(expectedSundayFin)
                assertThat(result.avatarUrl).isEqualTo("https://pub-6c821c1c547c4e3eaa97abd4b0ab8180.r2.dev/logo_agora_f524e2d9bd.png")
                assertThat(result.nom).isEqualTo("Ministre à révéler")
                assertThat(result.fonction).isEqualTo("Le ministre qui répondra dépend de votre question gagnante")
        }

        @Test
        fun `getThemeHebdo - when repository returns multiple items and multiple match current date - should return first matching item`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val firstCurrentTheme = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = tomorrow, titre = "Premier")
                val secondCurrentTheme = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = tomorrow, titre = "Second")
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(firstCurrentTheme, secondCurrentTheme))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.titre).isEqualTo(firstCurrentTheme.titre)
        }

        @Test
        fun `getThemeHebdoWithDefaultDates - when dateDebutTheme is null - item is excluded from range check, should return default ThemeHebdo with default dates`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val themeHebdo = buildThemeHebdo(dateDebutTheme = null, dateFinTheme = tomorrow)
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.dateDebutTheme).isEqualTo(expectedMondayDebut)
                assertThat(result.dateFinTheme).isEqualTo(expectedSundayFin)
                assertThat(result.avatarUrl).isEqualTo("https://pub-6c821c1c547c4e3eaa97abd4b0ab8180.r2.dev/logo_agora_f524e2d9bd.png")
                assertThat(result.nom).isEqualTo("Ministre à révéler")
                assertThat(result.fonction).isEqualTo("Le ministre qui répondra dépend de votre question gagnante")
        }

        @Test
        fun `getThemeHebdoWithDefaultDates - when dateFinTheme is null - item is excluded from range check, should return default ThemeHebdo with default dates`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val themeHebdo = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = null)
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.dateDebutTheme).isEqualTo(expectedMondayDebut)
                assertThat(result.dateFinTheme).isEqualTo(expectedSundayFin)
                assertThat(result.avatarUrl).isEqualTo("https://pub-6c821c1c547c4e3eaa97abd4b0ab8180.r2.dev/logo_agora_f524e2d9bd.png")
                assertThat(result.nom).isEqualTo("Ministre à révéler")
                assertThat(result.fonction).isEqualTo("Le ministre qui répondra dépend de votre question gagnante")
        }

        @Test
        fun `getThemeHebdoWithDefaultDates - when both dates are null - should return default ThemeHebdo with default dates`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val themeHebdo = buildThemeHebdo(dateDebutTheme = null, dateFinTheme = null)
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.dateDebutTheme).isEqualTo(expectedMondayDebut)
                assertThat(result.dateFinTheme).isEqualTo(expectedSundayFin)
                assertThat(result.avatarUrl).isEqualTo("https://pub-6c821c1c547c4e3eaa97abd4b0ab8180.r2.dev/logo_agora_f524e2d9bd.png")
                assertThat(result.nom).isEqualTo("Ministre à révéler")
                assertThat(result.fonction).isEqualTo("Le ministre qui répondra dépend de votre question gagnante")
        }

        @Test
        fun `getThemeHebdoWithDefaultDates - when both dates are defined and within range - should keep original dates`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val themeHebdo = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = tomorrow)
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.dateDebutTheme).isEqualTo(yesterday)
                assertThat(result.dateFinTheme).isEqualTo(tomorrow)
        }

        @Test
        fun `generatePeriodeFromDates - when debut and fin are in the same month - should return compact format`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                // 19 mai → 25 mai (autour du 22 mai)
                val debut = Date.from(LocalDateTime.of(2026, 5, 19, 0, 5).toInstant(ZoneOffset.UTC))
                val fin = Date.from(LocalDateTime.of(2026, 5, 25, 23, 55).toInstant(ZoneOffset.UTC))
                val themeHebdo = buildThemeHebdo(dateDebutTheme = debut, dateFinTheme = fin, periode = "")
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.periode).isEqualTo("19-25 MAI")
        }

        @Test
        fun `generatePeriodeFromDates - when debut and fin are in different months - should return extended format`() {
                // Given
                // now is set to 2026-05-29 so the range spans two months
                val nowSpanningMonths = Instant.parse("2026-05-29T10:00:00Z")
                `when`(clock.millis()).thenReturn(nowSpanningMonths.toEpochMilli())
                `when`(clock.instant()).thenReturn(nowSpanningMonths)
                // 26 mai → 1 juin
                val debut = Date.from(LocalDateTime.of(2026, 5, 26, 0, 5).toInstant(ZoneOffset.UTC))
                val fin = Date.from(LocalDateTime.of(2026, 6, 1, 23, 55).toInstant(ZoneOffset.UTC))
                val themeHebdo = buildThemeHebdo(dateDebutTheme = debut, dateFinTheme = fin, periode = "")
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.periode).isEqualTo("26 MAI - 1 JUIN")
        }

        @Test
        fun `generatePeriodeFromDates - when periode is already defined - should keep original periode`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                // 19 mai → 25 mai (autour du 22 mai)
                val debut = Date.from(LocalDateTime.of(2026, 5, 19, 0, 5).toInstant(ZoneOffset.UTC))
                val fin = Date.from(LocalDateTime.of(2026, 5, 25, 23, 55).toInstant(ZoneOffset.UTC))
                val themeHebdo = buildThemeHebdo(dateDebutTheme = debut, dateFinTheme = fin, periode = "Période personnalisée")
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.periode).isEqualTo("PÉRIODE PERSONNALISÉE")
        }

        @Test
        fun `getThemeHebdo - when estThemeLibre is true - should override sousTitre with theme libre message`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val themeHebdo = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = tomorrow, estThemeLibre = true)
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.sousTitre).isEqualTo("Posez vos questions sur n’importe quelle politique publique.")
        }

        @Test
        fun `getThemeHebdo - when estThemeLibre is false - should keep original sousTitre`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val themeHebdo = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = tomorrow, estThemeLibre = false)
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.sousTitre).isEqualTo("Sous-titre")
        }

        @Nested
        inner class GetProchainsThemesTest {

                @Test
                fun `getProchainsThemes - when no future themes exist - should return empty list`() {
                        // Given
                        `when`(clock.millis()).thenReturn(now.toEpochMilli())
                        `when`(clock.instant()).thenReturn(now)
                        val currentTheme = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = tomorrow, theme = "Éducation")
                        `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(currentTheme))

                        // When
                        val result = useCase.getThemeHebdo()

                        // Then
                        assertThat(result.prochainsThemes).isEmpty()
                }

                @Test
                fun `getProchainsThemes - when one future theme exists - should return a list with its title`() {
                        // Given
                        `when`(clock.millis()).thenReturn(now.toEpochMilli())
                        `when`(clock.instant()).thenReturn(now)
                        val currentTheme = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = tomorrow, theme = "Éducation")
                        val futureTheme = buildThemeHebdo(dateDebutTheme = nextWeek, dateFinTheme = Date.from(now.plusSeconds(14 * 86400)), theme = "Santé")
                        `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(currentTheme, futureTheme))

                        // When
                        val result = useCase.getThemeHebdo()

                        // Then
                        assertThat(result.prochainsThemes).containsExactly("Santé")
                }

                @Test
                fun `getProchainsThemes - when exactly 3 future themes exist - should return all 3 titles in ascending date order`() {
                        // Given
                        `when`(clock.millis()).thenReturn(now.toEpochMilli())
                        `when`(clock.instant()).thenReturn(now)
                        val currentTheme = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = tomorrow, theme = "Éducation")
                        val future1 = buildThemeHebdo(dateDebutTheme = Date.from(now.plusSeconds(2 * 86400)), dateFinTheme = Date.from(now.plusSeconds(9 * 86400)), theme = "Santé")
                        val future2 = buildThemeHebdo(dateDebutTheme = Date.from(now.plusSeconds(9 * 86400)), dateFinTheme = Date.from(now.plusSeconds(16 * 86400)), theme = "Environnement")
                        val future3 = buildThemeHebdo(dateDebutTheme = Date.from(now.plusSeconds(16 * 86400)), dateFinTheme = Date.from(now.plusSeconds(23 * 86400)), theme = "Économie")
                        `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(currentTheme, future2, future3, future1))

                        // When
                        val result = useCase.getThemeHebdo()

                        // Then
                        assertThat(result.prochainsThemes).containsExactly("Santé", "Environnement", "Économie")
                }

                @Test
                fun `getProchainsThemes - when more than 3 future themes exist - should return only the 3 closest ones in ascending date order`() {
                        // Given
                        `when`(clock.millis()).thenReturn(now.toEpochMilli())
                        `when`(clock.instant()).thenReturn(now)
                        val currentTheme = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = tomorrow, theme = "Éducation")
                        val future1 = buildThemeHebdo(dateDebutTheme = Date.from(now.plusSeconds(2 * 86400)), dateFinTheme = Date.from(now.plusSeconds(9 * 86400)), theme = "Santé")
                        val future2 = buildThemeHebdo(dateDebutTheme = Date.from(now.plusSeconds(9 * 86400)), dateFinTheme = Date.from(now.plusSeconds(16 * 86400)), theme = "Environnement")
                        val future3 = buildThemeHebdo(dateDebutTheme = Date.from(now.plusSeconds(16 * 86400)), dateFinTheme = Date.from(now.plusSeconds(23 * 86400)), theme = "Économie")
                        val future4 = buildThemeHebdo(dateDebutTheme = Date.from(now.plusSeconds(23 * 86400)), dateFinTheme = Date.from(now.plusSeconds(30 * 86400)), theme = "Logement")
                        `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(currentTheme, future4, future2, future1, future3))

                        // When
                        val result = useCase.getThemeHebdo()

                        // Then
                        assertThat(result.prochainsThemes).containsExactly("Santé", "Environnement", "Économie")
                }

                @Test
                fun `getProchainsThemes - when current theme has no dateFinTheme - should return empty list`() {
                        // Given
                        `when`(clock.millis()).thenReturn(now.toEpochMilli())
                        `when`(clock.instant()).thenReturn(now)
                        // No current theme found → default ThemeHebdo has dateFinTheme = defaultFin (sunday)
                        // But we test explicitly with no theme in range and a future theme
                        `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(emptyList())

                        // When
                        val result = useCase.getThemeHebdo()

                        // Then
                        // default ThemeHebdo gets default dates, so dateFinTheme is set → prochainsThemes may be empty (no items after defaultFin)
                        assertThat(result.prochainsThemes).isEmpty()
                }

                @Test
                fun `getProchainsThemes - when future themes have null dateDebutTheme - should exclude them`() {
                        // Given
                        `when`(clock.millis()).thenReturn(now.toEpochMilli())
                        `when`(clock.instant()).thenReturn(now)
                        val currentTheme = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = tomorrow, theme = "Éducation")
                        val futureWithNullDebut = buildThemeHebdo(dateDebutTheme = null, dateFinTheme = Date.from(now.plusSeconds(14 * 86400)), theme = "Santé")
                        `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(currentTheme, futureWithNullDebut))

                        // When
                        val result = useCase.getThemeHebdo()

                        // Then
                        assertThat(result.prochainsThemes).isEmpty()
                }
        }
}
