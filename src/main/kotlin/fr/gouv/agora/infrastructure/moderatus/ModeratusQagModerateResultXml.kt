package fr.gouv.agora.infrastructure.moderatus

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement

@JsonIgnoreProperties(ignoreUnknown = true)
@JacksonXmlRootElement(localName = "contents")
data class ModeratusQagModerateResultPageXml(
    @field:JacksonXmlProperty(localName = "result")
    val result: String,
    @field:JacksonXmlProperty(localName = "error")
    val error: String?,
)
