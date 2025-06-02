package fr.gouv.agora.infrastructure.consultation.dto.strapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import fr.gouv.agora.infrastructure.common.StrapiDataNullable
import fr.gouv.agora.infrastructure.common.StrapiMediaPicture
import fr.gouv.agora.infrastructure.common.StrapiMediaVideo
import fr.gouv.agora.infrastructure.common.StrapiRichText
import java.time.LocalDate

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "__component",
    visible = true
)
@JsonSubTypes(
    JsonSubTypes.Type(
        value = StrapiConsultationSectionTitre::class,
        name = "consultation-section.section-titre"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationSectionRichText::class,
        name = "consultation-section.section-texte-riche"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationSectionCitation::class,
        name = "consultation-section.section-citation"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationSectionImage::class,
        name = "consultation-section.section-image"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationSectionVideo::class,
        name = "consultation-section.section-video"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationSectionChiffre::class,
        name = "consultation-section.section-chiffre"
    ),
    JsonSubTypes.Type(
        value = StrapiConsultationSectionAccordeon::class,
        name = "consultation-section.section-accordeon"
    ),
)
@JsonIgnoreProperties("__component", "createdAt", "updatedAt", "publishedAt")
sealed interface StrapiConsultationSection

data class StrapiConsultationSectionCitation(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("description")
    val description: List<StrapiRichText>,
) : StrapiConsultationSection

data class StrapiConsultationSectionImage(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("url")
    val url: String,
    @JsonProperty("description_accessible_de_l_image")
    val descriptionImage: String,
    @JsonProperty(value = "image")
    val image: StrapiDataNullable<StrapiMediaPicture>,
) : StrapiConsultationSection {
    fun getImageUrl(): String {
        return if (image.data == null) url
        else image.data.attributes.getUrl()
    }
}

data class StrapiConsultationSectionRichText(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("description")
    val description: List<StrapiRichText>,
) : StrapiConsultationSection

data class StrapiConsultationSectionTitre(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("titre")
    val titre: String,
) : StrapiConsultationSection

data class StrapiConsultationSectionChiffre(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("description")
    val description: List<StrapiRichText>,
) : StrapiConsultationSection

data class StrapiConsultationSectionAccordeon(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("titre")
    val titre: String,
    @JsonProperty("description")
    val description: List<StrapiRichText>,
): StrapiConsultationSection

data class StrapiConsultationSectionVideo(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("url")
    val url: String,
    @JsonProperty("largeur")
    val largeur: Int,
    @JsonProperty("hauteur")
    val hauteur: Int,
    @JsonProperty("nom_auteur")
    val nomAuteur: String,
    @JsonProperty("poste_auteur")
    val posteAuteur: String,
    @JsonProperty("date_tournage")
    val dateTournage: LocalDate,
    @JsonProperty("transcription")
    val transcription: String,
    @JsonProperty(value = "video")
    val video: StrapiDataNullable<StrapiMediaVideo>,
) : StrapiConsultationSection {
    fun getVideoUrl(): String {
        return if (video.data == null) url
        else video.data.attributes.url
    }
}
