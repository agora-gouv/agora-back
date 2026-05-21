package fr.gouv.agora.usecase.themeHebdo

import java.time.LocalDate
import java.time.ZoneId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class GetThemeHebdoUseCaseTest {

        @InjectMocks private lateinit var useCase: GetThemeHebdoUseCase

        @Test
        fun `getThemeHebdo - when called - should return a ThematiqueHebdo with expected fields`() {
                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result.titre).isEqualTo("Cette semaine")
                assertThat(result.sousTitre).isEqualTo("Cette semaine posez vos questions sur...")
                assertThat(result.periode).isEqualTo("19-25 mai 2026")
                assertThat(result.theme).isEqualTo("Éducation")
                assertThat(result.avatarUrl).isEqualTo("https://picsum.photos/40")
                assertThat(result.nom).isEqualTo("Jean Dupont")
                assertThat(result.fonction).isEqualTo("Ministre de l'Éducation nationale")
                assertThat(result.prochainsThemes)
                        .isEqualTo(listOf("Santé", "Environnement", "Économie"))
                assertThat(result.titreCompteur).isEqualTo("Cloture des votes")
                assertThat(result.dateFinTheme)
                        .isEqualTo(
                                java.util.Date.from(
                                        LocalDate.of(2026, 5, 25)
                                                .atStartOfDay(ZoneId.of("Europe/Paris"))
                                                .toInstant()
                                )
                        )
        }
}
