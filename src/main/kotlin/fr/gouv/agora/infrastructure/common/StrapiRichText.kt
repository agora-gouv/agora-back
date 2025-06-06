package fr.gouv.agora.infrastructure.common

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import org.slf4j.Logger
import org.slf4j.LoggerFactory


// Pour définir les types,
// je me suis inspiré du thread https://forum.strapi.io/t/blocks-rich-text-editor/32588/5 sur le forum Strapi
// et de la lib React https://github.com/strapi/blocks-react-renderer/
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    property = "type",
    visible = true,
    defaultImpl = StrapiRichUnknownNode::class
)
@JsonSubTypes(
    JsonSubTypes.Type(value = StrapiRichTextNode::class, name = "text"),
    JsonSubTypes.Type(value = StrapiRichLinkNode::class, name = "link"),
    JsonSubTypes.Type(value = StrapiRichListItemNode::class, name = "list-item"),
    JsonSubTypes.Type(value = StrapiRichHeadingNode::class, name = "heading"),
    JsonSubTypes.Type(value = StrapiRichParagraphNode::class, name = "paragraph"),
    JsonSubTypes.Type(value = StrapiRichListNode::class, name = "list"),
    JsonSubTypes.Type(value = StrapiRichQuoteNode::class, name = "quote"),
)
@JsonIgnoreProperties(ignoreUnknown = true)
sealed interface StrapiRichText {
    fun toHtml(): String
}

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
) : StrapiRichText {
    override fun toHtml(): String {
        var htmlText = text
        if (bold != null && bold) {
            htmlText = "<b>$htmlText</b>"
        }
        if (italic != null && italic) {
            htmlText = "<i>$htmlText</i>"
        }
        if (underline != null && underline) {
            htmlText = "<u>$htmlText</u>"
        }
        if (strikethrough != null && strikethrough) {
            htmlText = "<del>$htmlText</del>"
        }
        if (code != null && code) {
            htmlText = "<code>$htmlText</code>"
        }

        return htmlText
    }
}

data class StrapiRichLinkNode(
    @JsonProperty("url")
    val url: String,
    @JsonProperty("children")
    val children: List<StrapiRichTextNode>,
) : StrapiRichText {
    override fun toHtml(): String {
        return "<a href=\"$url\">${children.toHtml()}</a>"
    }
}

data class StrapiRichListItemNode(
    @JsonProperty("children")
    val children: List<StrapiRichText>, // RichText | RichLink
) : StrapiRichText {
    override fun toHtml(): String {
        return "<li>${children.toHtml()}</li>"
    }
}

data class StrapiRichHeadingNode(
    @JsonProperty("level")
    val level: Int?,
    @JsonProperty("children")
    val children: List<StrapiRichText>, // TextNode | LinkNode
) : StrapiRichText {
    private val logger: Logger = LoggerFactory.getLogger(StrapiRichHeadingNode::class.java)

    override fun toHtml(): String {
        return when (level) {
            1 -> "<h1>${children.toHtml()}</h1>"
            2 -> "<h2>${children.toHtml()}</h2>"
            3 -> "<h3>${children.toHtml()}</h3>"
            4 -> "<h4>${children.toHtml()}</h4>"
            5 -> "<h5>${children.toHtml()}</h5>"
            6 -> "<h6>${children.toHtml()}</h6>"
            else -> {
                logger.warn("Erreur dans la conversion Strapi des titres, level '$level' non reconnu")
                return children.toHtml()
            }
        }
    }
}

data class StrapiRichListNode(
    @JsonProperty("format")
    val format: String, // 'unordered' | 'ordered'
    @JsonProperty("children")
    val children: List<StrapiRichText>,
) : StrapiRichText {
    override fun toHtml(): String {
        if (format == "ordered") {
            return "<ol>${children.toHtml()}</ol>"
        }

        return "<ul>${children.toHtml()}</ul>"
    }
}

data class StrapiRichParagraphNode(
    @JsonProperty("children")
    val children: List<StrapiRichText>, // TextNode | LinkNode
) : StrapiRichText {
    override fun toHtml(): String {
        return "${children.toHtml()}<br/><br/>"
    }
}

data class StrapiRichQuoteNode(
    @JsonProperty("children")
    val children: List<StrapiRichText>, // TextNode | LinkNode
) : StrapiRichText {
    override fun toHtml(): String {
        return "<blockquote>${children.toHtml()}</blockquote>"
    }
}

data class StrapiRichUnknownNode(
    @JsonProperty("children")
    val children: List<StrapiRichText>,
) : StrapiRichText {
    override fun toHtml(): String {
        return children.toHtml()
    }
}

fun List<StrapiRichText>.toHtml(): String {
    return this.joinToString("") { it.toHtml() }
        .removeSuffix("<br/>")
}

fun List<StrapiRichText>.toHtmlBody(): String {
    return "<body>${this.toHtml()}</body>"
}
