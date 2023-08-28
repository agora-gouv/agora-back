package fr.social.gouv.agora.infrastructure.moderatus

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "contents")
data class ModeratusQagListXml(
    @field:JacksonXmlProperty(localName = "nb_content")
    val qagToModerateCount: Int,
    @field:JacksonXmlProperty(localName = "msg")
    val qagsToModerate: List<ModeratusQagXml>,
)

data class ModeratusQagXml(
    @field:JacksonXmlProperty(localName = "content_id")
    val qagId: String,
    @field:JacksonXmlProperty(localName = "date")
    val postDate: String,
    @field:JacksonXmlProperty(localName = "user_id")
    val userId: String,
    @field:JacksonXmlProperty(localName = "pseudo")
    val username: String,
    @field:JacksonXmlProperty(localName = "title")
    val title: String,
    @field:JacksonXmlProperty(localName = "body")
    val description: String,
    @field:JacksonXmlProperty(localName = "message_type")
    val messageType: String = "question",
)

@JacksonXmlRootElement(localName = "contents")
data class ModeratusQagListErrorXml(
    @field:JacksonXmlProperty(localName = "nb_content")
    val qagToModerateCount: Int = 0,
    @field:JacksonXmlProperty(localName = "error")
    val errorMessage: String,
)