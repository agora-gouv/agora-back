package fr.gouv.agora.usecase.themeHebdo

import fr.gouv.agora.domain.ThemeHebdo
import fr.gouv.agora.usecase.themeHebdo.repository.ThemeHebdoRepository
import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.Date
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
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
                periode: String = "19-25 mai 2026",
        ) = ThemeHebdo(
                titre = titre,
                sousTitre = "Sous-titre",
                periode = periode,
                theme = "Éducation",
                avatarUrl = "https://picsum.photos/40",
                nom = "Jean Dupont",
                fonction = "Ministre de l'Éducation nationale",
                prochainsThemes = listOf("Santé", "Environnement", "Économie"),
                titreCompteur = "Cloture des votes",
                dateDebutTheme = dateDebutTheme,
                dateFinTheme = dateFinTheme,
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
        }

        @Test
        fun `getThemeHebdo - when repository returns exactly one item - should return it without date check`() {
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
        fun `getThemeHebdoWithDefaultDates - when dateDebutTheme is null - should set it to monday 0h05 UTC`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val themeHebdo = buildThemeHebdo(dateDebutTheme = null, dateFinTheme = tomorrow)
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.dateDebutTheme).isEqualTo(expectedMondayDebut)
                assertThat(result.dateFinTheme).isEqualTo(tomorrow)
        }

        @Test
        fun `getThemeHebdoWithDefaultDates - when dateFinTheme is null - should set it to sunday 23h55 UTC`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val themeHebdo = buildThemeHebdo(dateDebutTheme = yesterday, dateFinTheme = null)
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.dateDebutTheme).isEqualTo(yesterday)
                assertThat(result.dateFinTheme).isEqualTo(expectedSundayFin)
        }

        @Test
        fun `getThemeHebdoWithDefaultDates - when both dates are null - should set both to monday 0h05 and sunday 23h55 UTC`() {
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
        }

        @Test
        fun `getThemeHebdoWithDefaultDates - when both dates are defined - should keep original dates`() {
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
                // 10 mai → 16 mai
                val debut = Date.from(LocalDateTime.of(2026, 5, 10, 0, 5).toInstant(ZoneOffset.UTC))
                val fin = Date.from(LocalDateTime.of(2026, 5, 16, 23, 55).toInstant(ZoneOffset.UTC))
                val themeHebdo = buildThemeHebdo(dateDebutTheme = debut, dateFinTheme = fin, periode = "")
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.periode).isEqualTo("10-16 mai")
        }

        @Test
        fun `generatePeriodeFromDates - when debut and fin are in different months - should return extended format`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                // 24 mai → 1 juin
                val debut = Date.from(LocalDateTime.of(2026, 5, 24, 0, 5).toInstant(ZoneOffset.UTC))
                val fin = Date.from(LocalDateTime.of(2026, 6, 1, 23, 55).toInstant(ZoneOffset.UTC))
                val themeHebdo = buildThemeHebdo(dateDebutTheme = debut, dateFinTheme = fin, periode = "")
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.periode).isEqualTo("24 mai - 1 juin")
        }

        @Test
        fun `generatePeriodeFromDates - when periode is already defined - should keep original periode`() {
                // Given
                `when`(clock.millis()).thenReturn(now.toEpochMilli())
                `when`(clock.instant()).thenReturn(now)
                val debut = Date.from(LocalDateTime.of(2026, 5, 10, 0, 5).toInstant(ZoneOffset.UTC))
                val fin = Date.from(LocalDateTime.of(2026, 5, 16, 23, 55).toInstant(ZoneOffset.UTC))
                val themeHebdo = buildThemeHebdo(dateDebutTheme = debut, dateFinTheme = fin, periode = "Période personnalisée")
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.periode).isEqualTo("Période personnalisée")
        }
}
