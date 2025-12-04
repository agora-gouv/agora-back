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

    inner class FiltersDsl internal constructor() {
        fun addThematique(thematique: String?) = apply { andThematique(thematique) }
        fun addEtape(etape: List<String>?) = apply { andEtape(etape) }
        fun addModalite(modaliteParticipation: List<String>?) = apply { andModalite(modaliteParticipation) }
        fun addAnneeDeLancement(annee: String?) = apply { andAnneeDeLancement(annee) }
        fun addTitre(titre: String?) = apply { andTitre(titre) }
    }
    fun filters(attribut: FiltersDsl.() -> Unit): StrapiRequestBuilder {
        FiltersDsl().apply(attribut)
        return this
    }
    fun andThematique(thematique: String?) {
        if (thematique != null) {
            filterBy(listOf("thematique", "id"), listOf(thematique))
        }
    }
    fun andEtape(etape: List<String>?) {
        if (!etape.isNullOrEmpty()) {
            filterBy("etape", etape)
        }
    }
    fun andModalite(modaliteParticipation: List<String>?) {
        if (!modaliteParticipation.isNullOrEmpty()) {
            filterBy("modalite_participation", modaliteParticipation)
        }
    }
    fun andAnneeDeLancement(anneeDeLancement: String?) {
        if (anneeDeLancement != null) {
            filterBy("annee_de_lancement", listOf(anneeDeLancement))
        }
    }
    fun andTitre(titre: String?) {
        if (titre != null) {
            withContains("titre", listOf(titre))
        }
    }
    fun filterBy(fields: List<String>, values: List<String>): StrapiRequestBuilder {
        val fieldParam = fields.joinToString("") { "[$it]" }
        if (values.isEmpty()) {
            builderError = "filterBy : aucune valeur donnée pour le champs $fieldParam"
            return this
        }

        if (values.size > 80)
            logger.warn("attention : ne peut pas gérer plus de ~100 filtres dans l'url (${values.size}/100)")

        filters += values
            .map { URLEncoder.encode(it, UTF_8) }
            .joinToString("") { "&filters$fieldParam[\$in]=$it" }

        return this
    }
    fun filterBy(field: String, values: List<String>): StrapiRequestBuilder {
        return this.filterBy(listOf(field), values)
    }
    fun withContains(field: String, values: List<String>): StrapiRequestBuilder {
        if (values.isEmpty()) {
            builderError = "filterBy : aucune valeur donnée pour le champs $field"
            return this
        }

        if (values.size > 80)
            logger.warn("attention : ne peut pas gérer plus de ~100 filtres dans l'url (${values.size}/100)")

        filters += values
                .map { URLEncoder.encode(it, UTF_8) }
                .joinToString("") { "&filters$field[\$contains]=$it" }

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
