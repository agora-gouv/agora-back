package fr.gouv.agora.infrastructure.moderatus

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JacksonXmlRootElement(localName = "contents")
data class ModeratusQagLockResultsXml(
    @field:JacksonXmlProperty(localName = "confirmation")
    @JacksonXmlElementWrapper(useWrapping = false)
    val qagLockedResults: List<ModeratusQagLockResultXml>,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ModeratusQagLockResultXml(
    @field:JacksonXmlProperty(localName = "content_id", isAttribute = true)
    val qagId: String,
    @field:JacksonXmlProperty(localName = "result", isAttribute = true)
    val result: String,
    @field:JacksonXmlProperty(localName = "comment", isAttribute = true)
    val comment: String?,
)
