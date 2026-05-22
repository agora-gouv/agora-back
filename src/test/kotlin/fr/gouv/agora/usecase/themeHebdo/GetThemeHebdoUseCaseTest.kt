package fr.gouv.agora.usecase.themeHebdo

import fr.gouv.agora.domain.ThemeHebdo
import fr.gouv.agora.usecase.themeHebdo.repository.ThemeHebdoRepository
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

        private val themeHebdo =
                ThemeHebdo(
                        titre = "Cette semaine",
                        sousTitre = "Cette semaine posez vos questions sur...",
                        periode = "19-25 mai 2026",
                        theme = "Éducation",
                        avatarUrl = "https://picsum.photos/40",
                        nom = "Jean Dupont",
                        fonction = "Ministre de l'Éducation nationale",
                        prochainsThemes = listOf("Santé", "Environnement", "Économie"),
                        titreCompteur = "Cloture des votes",
                        dateFinTheme = Date(),
                        dateDebutTheme = Date()
                )

        @Test
        fun `getThemeHebdo - when repository returns a non-empty list - should return the first item`() {
                // Given
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(listOf(themeHebdo))

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result).isEqualTo(themeHebdo)
        }

        @Test
        fun `getThemeHebdo - when repository returns an empty list - should return null`() {
                // Given
                `when`(themeHebdoRepository.getThemeHebdoList()).thenReturn(emptyList())

                // When
                val result = useCase.getThemeHebdo()

                // Then
                assertThat(result).isNull()
        }
}
