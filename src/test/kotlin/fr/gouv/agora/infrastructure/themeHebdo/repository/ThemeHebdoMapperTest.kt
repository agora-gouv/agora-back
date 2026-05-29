package fr.gouv.agora.infrastructure.themeHebdo.repository

import fr.gouv.agora.infrastructure.common.StrapiAttributes
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiDataNullable
import fr.gouv.agora.infrastructure.common.StrapiMediaPicture
import fr.gouv.agora.infrastructure.common.StrapiMediaPictureFormats
import fr.gouv.agora.infrastructure.common.StrapiMediaPictureFormatMedium
import fr.gouv.agora.infrastructure.common.StrapiMetadata
import fr.gouv.agora.infrastructure.common.StrapiMetaPagination
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
internal class ThemeHebdoMapperTest {

    private lateinit var mapper: ThemeHebdoMapper

    @BeforeEach
    fun setUp() {
        mapper = ThemeHebdoMapper()
    }

    @Nested
    inner class `toDomain - photo` {

        @Test
        fun `toDomain - when photo data is null - should return avatarUrl null`() {
            // Given
            val dto = buildStrapiDTO(photo = StrapiDataNullable(data = null))

            // When
            val result = mapper.toDomain(dto)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].avatarUrl).isNull()
        }

        @Test
        fun `toDomain - when photo has medium format - should return medium url`() {
            // Given
            val mediumUrl = "https://example.com/medium.jpg"
            val photo = StrapiDataNullable(
                data = StrapiAttributes(
                    id = "1",
                    attributes = StrapiMediaPicture(
                        formats = StrapiMediaPictureFormats(
                            medium = StrapiMediaPictureFormatMedium(url = mediumUrl)
                        ),
                        pictureUrlNotOptimized = "https://example.com/original.jpg"
                    )
                )
            )
            val dto = buildStrapiDTO(photo = photo)

            // When
            val result = mapper.toDomain(dto)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].avatarUrl).isEqualTo(mediumUrl)
        }

        @Test
        fun `toDomain - when photo has no medium format - should return raw url`() {
            // Given
            val rawUrl = "https://example.com/original.jpg"
            val photo = StrapiDataNullable(
                data = StrapiAttributes(
                    id = "1",
                    attributes = StrapiMediaPicture(
                        formats = StrapiMediaPictureFormats(medium = null),
                        pictureUrlNotOptimized = rawUrl
                    )
                )
            )
            val dto = buildStrapiDTO(photo = photo)

            // When
            val result = mapper.toDomain(dto)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].avatarUrl).isEqualTo(rawUrl)
        }
    }

    @Nested
    inner class `toDomain - other fields` {

        @Test
        fun `toDomain - when valid dto - should map all fields correctly`() {
            // Given
            val dto = buildStrapiDTO(
                theme = "Santé",
                periode = "19-25 mai 2026",
                nomMinistre = "Jean Dupont",
                fonction = "Ministre de la santé",
                dateDebut = "2026-05-19T00:00:00+02:00",
                dateFin = "2026-05-25T23:59:00+02:00",
                photo = StrapiDataNullable(data = null)
            )

            // When
            val result = mapper.toDomain(dto)

            // Then
            assertThat(result).hasSize(1)
            val themeHebdo = result[0]
            assertThat(themeHebdo.theme).isEqualTo("Santé")
            assertThat(themeHebdo.periode).isEqualTo("19-25 mai 2026")
            assertThat(themeHebdo.nom).isEqualTo("Jean Dupont")
            assertThat(themeHebdo.fonction).isEqualTo("Ministre de la santé")
            assertThat(themeHebdo.dateDebutTheme).isNotNull
            assertThat(themeHebdo.dateFinTheme).isNotNull
        }

        @Test
        fun `toDomain - when periode is null - should return empty string for periode`() {
            // Given
            val dto = buildStrapiDTO(periode = null, photo = StrapiDataNullable(data = null))

            // When
            val result = mapper.toDomain(dto)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].periode).isEqualTo("")
        }
    }

    @Nested
    inner class `toDomain - est_theme_libre` {

        @Test
        fun `toDomain - when est_theme_libre is true - should set estThemeLibre to true`() {
            // Given
            val dto = buildStrapiDTO(estThemeLibre = true)

            // When
            val result = mapper.toDomain(dto)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].estThemeLibre).isTrue()
        }

        @Test
        fun `toDomain - when est_theme_libre is false - should set estThemeLibre to false`() {
            // Given
            val dto = buildStrapiDTO(estThemeLibre = false)

            // When
            val result = mapper.toDomain(dto)

            // Then
            assertThat(result).hasSize(1)
            assertThat(result[0].estThemeLibre).isFalse()
        }
    }

    private fun buildStrapiDTO(
        theme: String = "Thème libre",
        periode: String? = "19-25 mai 2026",
        nomMinistre: String? = null,
        fonction: String? = null,
        dateDebut: String = "2026-05-19T00:00:00+02:00",
        dateFin: String = "2026-05-25T23:59:00+02:00",
        photo: StrapiDataNullable<StrapiMediaPicture> = StrapiDataNullable(data = null),
        estThemeLibre: Boolean = false,
    ): StrapiDTO<ThemeHebdoStrapiDTO> {
        return StrapiDTO(
            data = listOf(
                StrapiAttributes(
                    id = "1",
                    attributes = ThemeHebdoStrapiDTO(
                        theme = theme,
                        periode = periode,
                        photo = photo,
                        nom_ministre = nomMinistre,
                        fonction = fonction,
                        date_debut = dateDebut,
                        date_fin = dateFin,
                        est_theme_libre = estThemeLibre,
                    )
                )
            ),
            meta = StrapiMetadata(StrapiMetaPagination(page = 1, pageSize = 10, pageCount = 1, total = 1))
        )
    }
}
