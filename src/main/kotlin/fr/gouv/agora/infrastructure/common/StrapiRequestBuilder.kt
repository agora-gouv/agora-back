package fr.gouv.agora.infrastructure.common

import org.slf4j.LoggerFactory
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.text.Charsets.UTF_8

data class StrapiRequestBuilder(private val cmsModel: String) {
    private var filters = ""
    private var sort = ""
    private var populate = "&populate=deep"
    private var pageSize = "pagination[pageSize]=100"
    private var unpublished = ""

    private var builderError = ""

    private val logger = LoggerFactory.getLogger(StrapiRequestBuilder::class.java)

    fun filterBy(field: String, values: List<String>): StrapiRequestBuilder {
        if (values.isEmpty()) {
            builderError = "filterBy : aucune valeur donnée pour le champs $field"
            return this
        }

        if (values.size > 80)
            logger.warn("attention : ne peut pas gérer plus de ~100 filtres dans l'url (${values.size}/100)")

        filters += values
            .map { URLEncoder.encode(it, UTF_8) }
            .joinToString("") { "&filters[$field][\$in]=$it" }

        return this
    }

    fun getByIds(ids: List<Int>): StrapiRequestBuilder {
        return filterBy("id", ids.map { it.toString() })
    }

    fun withDateBefore(date: LocalDateTime, fieldBeforeDate: String): StrapiRequestBuilder {
        val formattedDate = date.format(DateTimeFormatter.ISO_DATE_TIME)
        filters += "&filters[$fieldBeforeDate][\$lt]=$formattedDate"

        return this
    }

    fun withDateAfter(date: LocalDateTime, fieldAfterDate: String): StrapiRequestBuilder {
        val formattedDate = date.format(DateTimeFormatter.ISO_DATE_TIME)
        filters += "&filters[$fieldAfterDate][\$gt]=$formattedDate"

        return this
    }

    fun sortBy(field: String, direction: String): StrapiRequestBuilder {
        sort += "&sort[0]=$field:$direction"
        return this
    }

    fun withPageSize(numberOfElements: Int): StrapiRequestBuilder {
        pageSize = "pagination[pageSize]=$numberOfElements"
        return this
    }

    fun populate(newPopulate: String): StrapiRequestBuilder {
        populate = "&populate=$newPopulate"
        return this
    }

    fun withUnpublished(): StrapiRequestBuilder {
        unpublished = "&publicationState=preview"
        return this
    }

    fun build(): String {
        if (builderError.isNotBlank()) throw Exception(builderError)

        return "${cmsModel}?$pageSize$unpublished$populate$filters$sort"
    }
}
