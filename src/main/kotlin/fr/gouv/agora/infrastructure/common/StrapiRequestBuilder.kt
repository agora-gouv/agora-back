package fr.gouv.agora.infrastructure.common

import org.slf4j.LoggerFactory
import java.net.URLEncoder
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.collections.isNullOrEmpty
import kotlin.text.Charsets.UTF_8
import kotlin.text.isNullOrEmpty

data class StrapiRequestBuilder(private val cmsModel: String) {
    private var filters = ""
    private var sort = ""
    private var populate = "&populate=*"
    private var pageSize = "pagination[pageSize]=100"
    private var unpublished = ""

    private var builderError = ""

    private val logger = LoggerFactory.getLogger(StrapiRequestBuilder::class.java)

    private fun applyFilter(key: String, operator: String, value: String?): StrapiRequestBuilder {
        if (value.isNullOrEmpty()) {
            builderError = "filterIn : aucune valeur donnée pour le champs $key"
            return this
        }
        val it = URLEncoder.encode(value, UTF_8)
        filters += "&filters$key[\$$operator]=$it"
        return this
    }
    fun filterIn(field: String, values: List<String?>?): StrapiRequestBuilder {
        return this.filterIn(listOf(field), values)
    }
    fun filterIn(field: List<String>, values: List<String?>?): StrapiRequestBuilder {
        val fieldParam = field.joinToString("") { "[$it]" }

        if (values != null && values.size > 80)
            logger.warn("attention : ne peut pas gérer plus de ~100 filtres dans l'url (${values.size}/100)")

        if (values.isNullOrEmpty()) {
            builderError = "filterIn : aucune valeur donnée pour le champs $field"
            return this
        }

        values.forEach { value -> applyFilter(fieldParam, "in", value) }

        return this
    }
    fun contains(field: String, value: String?): StrapiRequestBuilder {
        val fieldParam = field.let { "[$it]" }
        applyFilter(fieldParam, "containsi", value)

        return this
    }
    fun getByIds(ids: List<String>): StrapiRequestBuilder {
        return filterIn("documentId", ids)
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
        unpublished = "&status=draft"
        return this
    }

    fun build(): String {
        if (builderError.isNotBlank()) throw Exception(builderError)

        return "${cmsModel}?$pageSize$unpublished$populate$filters$sort"
    }
}
