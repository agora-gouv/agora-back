package fr.gouv.agora.infrastructure.responseQag.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import java.time.LocalDate

data class StrapiResponseQag(
    @JsonProperty(value = "auteur")
    val auteur: String,
    @JsonProperty(value = "auteurPortraitUrl")
    val auteurPortraitUrl: String,
    @JsonProperty(value = "reponseDate")
    val reponseDate: LocalDate,
    @JsonProperty(value = "feedbackQuestion")
    val feedbackQuestion: String,
    @JsonProperty(value = "questionId")
    val questionId: String,
    @JsonProperty("reponseType")
    val reponseType: List<StrapiResponseQagType>
)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "__component", visible = true)
@JsonSubTypes(
    JsonSubTypes.Type(value = StrapiResponseQagText::class, name = "reponse.reponsetextuelle"),
    JsonSubTypes.Type(value = StrapiResponseQagVideo::class, name = "reponse.reponse-video")
)
sealed interface StrapiResponseQagType

data class StrapiResponseQagText(
    @JsonProperty("label")
    val label: String,
    @JsonProperty("text")
    val text: List<StrapiRichText>?,
) : StrapiResponseQagType

data class StrapiResponseQagVideo(
    @JsonProperty("auteurDescription")
    val auteurDescription: String,
    @JsonProperty("urlVideo")
    val urlVideo: String,
    @JsonProperty("videoWidth")
    val videoWidth: Int,
    @JsonProperty("videoHeight")
    val videoHeight: Int,
    @JsonProperty("transcription")
    val transcription: String,
    @JsonProperty("informationAdditionnelleTitre")
    val informationAdditionnelleTitre: String?,
    @JsonProperty("informationAdditionnelleDescription")
    val informationAdditionnelleDescription: List<StrapiRichText>?,
) : StrapiResponseQagType

// Pour définir les types,
// je me suis inspiré du thread https://forum.strapi.io/t/blocks-rich-text-editor/32588/5 sur le forum Strapi
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes(
    JsonSubTypes.Type(value = StrapiRichTextNode::class, name = "text"),
    JsonSubTypes.Type(value = StrapiRichLinkNode::class, name = "link"),
    JsonSubTypes.Type(value = StrapiRichListItemNode::class, name = "list-item"),
    JsonSubTypes.Type(value = StrapiRichBlockNode::class, name = "heading"),
    JsonSubTypes.Type(value = StrapiRichBlockNode::class, name = "paragraph"),
    JsonSubTypes.Type(value = StrapiRichBlockNode::class, name = "list"),
    JsonSubTypes.Type(value = StrapiRichBlockNode::class, name = "quote"),
)
sealed interface StrapiRichText

//data class StrapiRichText(
//    @JsonProperty("text")
//    val text: String?,
//    @JsonProperty("type")
//    val type: String,
//    @JsonProperty("bold")
//    val bold: Boolean?,
//    @JsonProperty("underline")
//    val underline: Boolean?,
//    @JsonProperty("strikethrough")
//    val strikethrough: Boolean?,
//    @JsonProperty("children")
//    val children: List<StrapiRichText>?,
//    @JsonProperty("format")
//    val format: String?,
//    @JsonProperty("code")
//    val code: Boolean?,
//    @JsonProperty("level")
//    val level: Int?,
//)

// todo : soit j'utilise un type générique mais je dois checker les champs
// todo : soit j'utilise plusieurs types, que je peux dispatcher via le champs type (ça me permettrait de mieux gérer tous les cas)
data class StrapiRichTextNode(
    @JsonProperty("text")
    val text: String,
    @JsonProperty("bold")
    val bold: Boolean?,
    @JsonProperty("underline")
    val underline: Boolean?,
    @JsonProperty("italic")
    val italic: Boolean?,
    @JsonProperty("strikethrough")
    val strikethrough: Boolean?,
    @JsonProperty("code")
    val code: Boolean?,
)
data class StrapiRichLinkNode(
    @JsonProperty("url")
    val url: String,
    @JsonProperty("children")
    val children: List<StrapiRichTextNode>,
)

data class StrapiRichListItemNode(
    @JsonProperty("children")
    val children: List<StrapiRichText>, // en vrai on ne peut avoir que RichText ou RichLink
)
data class StrapiRichBlockNode(
    @JsonProperty("let pvel")
    val level: Int?,
    @JsonProperty("format")
    val format: String?, // 'unordered' | 'ordered'
    @JsonProperty("children")
    val children: List<StrapiRichText>, // TextNode | LinkNode | ListItemNode
)
