package fr.gouv.agora.infrastructure.responseQag.repository

import fr.gouv.agora.domain.ResponseQagVideo
import fr.gouv.agora.infrastructure.common.StrapiDTO
import fr.gouv.agora.infrastructure.common.StrapiMetaPagination
import fr.gouv.agora.infrastructure.common.StrapiMetadata
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQag
import fr.gouv.agora.infrastructure.responseQag.dto.StrapiResponseQagVideo
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.junit.jupiter.MockitoExtension
import java.time.LocalDate

@ExtendWith(MockitoExtension::class)
class ResponseQagMapperTest {

    @InjectMocks
    private lateinit var responseQagMapper: ResponseQagMapper

    @Nested
    inner class ToDomainVideoAuthorDescription {

        @Test
        fun `toDomain - when auteurFonction is not null - should use auteurFonction for both authorFunction and authorDescription`() {
            // Given
            val strapiVideo = StrapiResponseQagVideo(
                auteurDescription = "Description depuis Strapi video",
                urlVideo = "https://example.com/video.mp4",
                videoWidth = 1280,
                videoHeight = 720,
                transcription = "Transcription",
                informationAdditionnelleTitre = null,
                pageTitle = "Titre page",
                informationAdditionnelleDescription = null,
                video = null,
            )
            val strapiResponse = StrapiResponseQag(
                auteur = "Jean Dupont",
                auteurPortraitUrl = "https://example.com/portrait.jpg",
                auteurFonction = "Ministre de l'Économie",
                reponseDate = LocalDate.of(2024, 1, 15),
                feedbackQuestion = "Question feedback",
                questionId = "qag-123",
                reponseType = listOf(strapiVideo),
                auteurPortrait = null,
            )
            val strapiDTO = StrapiDTO(
                data = listOf(strapiResponse),
                meta = StrapiMetadata(StrapiMetaPagination(1, 10, 1, 1)),
            )

            // When
            val result = responseQagMapper.toDomain(strapiDTO)

            // Then
            assertThat(result).hasSize(1)
            val video = result.first() as ResponseQagVideo
            assertThat(video.authorFunction).isEqualTo("Ministre de l'Économie")
            assertThat(video.authorDescription).isEqualTo("Ministre de l'Économie")
        }

        @Test
        fun `toDomain - when auteurFonction is null - should use auteurDescription from video as fallback for both authorFunction and authorDescription`() {
            // Given
            val strapiVideo = StrapiResponseQagVideo(
                auteurDescription = "Description depuis Strapi video",
                urlVideo = "https://example.com/video.mp4",
                videoWidth = 1280,
                videoHeight = 720,
                transcription = "Transcription",
                informationAdditionnelleTitre = null,
                pageTitle = "Titre page",
                informationAdditionnelleDescription = null,
                video = null,
            )
            val strapiResponse = StrapiResponseQag(
                auteur = "Jean Dupont",
                auteurPortraitUrl = "https://example.com/portrait.jpg",
                auteurFonction = null,
                reponseDate = LocalDate.of(2024, 1, 15),
                feedbackQuestion = "Question feedback",
                questionId = "qag-123",
                reponseType = listOf(strapiVideo),
                auteurPortrait = null,
            )
            val strapiDTO = StrapiDTO(
                data = listOf(strapiResponse),
                meta = StrapiMetadata(StrapiMetaPagination(1, 10, 1, 1)),
            )

            // When
            val result = responseQagMapper.toDomain(strapiDTO)

            // Then
            assertThat(result).hasSize(1)
            val video = result.first() as ResponseQagVideo
            assertThat(video.authorFunction).isEqualTo("Description depuis Strapi video")
            assertThat(video.authorDescription).isEqualTo("Description depuis Strapi video")
        }
    }
}
