package fr.social.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.annotation.JsonProperty
import fr.social.gouv.agora.domain.*
import fr.social.gouv.agora.usecase.consultation.repository.ConsultationPreviewPageRepository
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import java.util.*

@Component
@Suppress("unused")
class ConsultationPreviewPageRepositoryImpl(
    private val cacheManager: CacheManager,
) : ConsultationPreviewPageRepository {

    companion object {
        private const val CONSULTATION_PREVIEW_PAGE_CACHE_NAME = "consultationPreviewPage"

        private const val ONGOING_CACHE_PREFIX = "ongoing"
        private const val FINISHED_CACHE_KEY = "finished"
        private const val ANSWERED_CACHE_PREFIX = "answered"
    }

    @Suppress("UNCHECKED_CAST")
    override fun getConsultationPreviewOngoingList(userId: String): List<ConsultationPreviewOngoing>? {
        return try {
            val modelList = getCache()?.get(
                "$ONGOING_CACHE_PREFIX$userId",
                List::class.java,
            ) as? List<ConsultationPreviewOngoingModel>
            return modelList?.map(::toOngoingPreview)
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun insertConsultationPreviewOngoingList(userId: String, ongoingList: List<ConsultationPreviewOngoing>) {
        getCache()?.put("$ONGOING_CACHE_PREFIX$userId", ongoingList.map(::toModel))
    }

    override fun evictConsultationPreviewOngoingList(userId: String) {
        getCache()?.evict("$ONGOING_CACHE_PREFIX$userId")
    }

    @Suppress("UNCHECKED_CAST")
    override fun getConsultationPreviewFinishedList(): List<ConsultationPreviewFinished>? {
        return try {
            val modelList = getCache()?.get(
                FINISHED_CACHE_KEY,
                List::class.java
            ) as? List<ConsultationPreviewWithStatusModel>
            return modelList?.map(::toFinishedPreview)
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun insertConsultationPreviewFinishedList(finishedList: List<ConsultationPreviewFinished>) {
        getCache()?.put(FINISHED_CACHE_KEY, finishedList.map(::toModel))
    }

    override fun evictConsultationPreviewFinishedList() {
        getCache()?.evict(FINISHED_CACHE_KEY)
    }

    @Suppress("UNCHECKED_CAST")
    override fun getConsultationPreviewAnsweredList(userId: String): List<ConsultationPreviewAnswered>? {
        return try {
            val modelList = getCache()?.get(
                ANSWERED_CACHE_PREFIX,
                List::class.java
            ) as? List<ConsultationPreviewWithStatusModel>
            return modelList?.map(::toAnsweredPreview)
        } catch (e: IllegalStateException) {
            null
        }
    }

    override fun insertConsultationPreviewAnsweredList(
        userId: String,
        answeredList: List<ConsultationPreviewAnswered>,
    ) {
        getCache()?.put("$ANSWERED_CACHE_PREFIX$userId", answeredList.map(::toModel))
    }

    override fun evictConsultationPreviewAnsweredList(userId: String) {
        getCache()?.evict("$ANSWERED_CACHE_PREFIX$userId")
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_PREVIEW_PAGE_CACHE_NAME)

    private fun toModel(consultation: ConsultationPreviewOngoing) = ConsultationPreviewOngoingModel(
        id = consultation.id,
        title = consultation.title,
        coverUrl = consultation.coverUrl,
        thematiqueId = consultation.thematique.id,
        thematiqueLabel = consultation.thematique.label,
        thematiquePicto = consultation.thematique.picto,
        endDateTime = consultation.endDate.time,
        highlightLabel = consultation.highlightLabel,
    )

    private fun toOngoingPreview(model: ConsultationPreviewOngoingModel) = ConsultationPreviewOngoing(
        id = model.id,
        title = model.title,
        coverUrl = model.coverUrl,
        thematique = Thematique(
            id = model.thematiqueId,
            label = model.thematiqueLabel,
            picto = model.thematiquePicto,
        ),
        endDate = Date(model.endDateTime),
        highlightLabel = model.highlightLabel,
    )

    private fun toModel(consultation: ConsultationPreviewFinished) = ConsultationPreviewWithStatusModel(
        id = consultation.id,
        title = consultation.title,
        coverUrl = consultation.coverUrl,
        thematiqueId = consultation.thematique.id,
        thematiqueLabel = consultation.thematique.label,
        thematiquePicto = consultation.thematique.picto,
        step = toStepModel(consultation.step),
    )

    private fun toFinishedPreview(model: ConsultationPreviewWithStatusModel) = ConsultationPreviewFinished(
        id = model.id,
        title = model.title,
        coverUrl = model.coverUrl,
        thematique = Thematique(
            id = model.thematiqueId,
            label = model.thematiqueLabel,
            picto = model.thematiquePicto,
        ),
        step = toStep(model.step),
    )

    private fun toModel(consultation: ConsultationPreviewAnswered) = ConsultationPreviewWithStatusModel(
        id = consultation.id,
        title = consultation.title,
        coverUrl = consultation.coverUrl,
        thematiqueId = consultation.thematique.id,
        thematiqueLabel = consultation.thematique.label,
        thematiquePicto = consultation.thematique.picto,
        step = toStepModel(consultation.step),
    )

    private fun toAnsweredPreview(model: ConsultationPreviewWithStatusModel) = ConsultationPreviewAnswered(
        id = model.id,
        title = model.title,
        coverUrl = model.coverUrl,
        thematique = Thematique(
            id = model.thematiqueId,
            label = model.thematiqueLabel,
            picto = model.thematiquePicto,
        ),
        step = toStep(model.step),
    )

    private fun toStepModel(consultationStatus: ConsultationStatus) = when (consultationStatus) {
        ConsultationStatus.COLLECTING_DATA -> 1
        ConsultationStatus.POLITICAL_COMMITMENT -> 2
        ConsultationStatus.EXECUTION -> 3
    }

    private fun toStep(step: Int) = when (step) {
        1 -> ConsultationStatus.COLLECTING_DATA
        2 -> ConsultationStatus.POLITICAL_COMMITMENT
        3 -> ConsultationStatus.EXECUTION
        else -> throw IllegalStateException("Invalid ConsultationStatus: $step")
    }
}

data class ConsultationPreviewOngoingModel(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("coverUrl")
    val coverUrl: String,
    @JsonProperty("thematiqueId")
    val thematiqueId: String,
    @JsonProperty("thematiqueLabel")
    val thematiqueLabel: String,
    @JsonProperty("thematiquePicto")
    val thematiquePicto: String,
    @JsonProperty("endDateLong")
    val endDateTime: Long,
    @JsonProperty("highlightLabel")
    val highlightLabel: String?,
)


data class ConsultationPreviewWithStatusModel(
    @JsonProperty("id")
    val id: String,
    @JsonProperty("title")
    val title: String,
    @JsonProperty("coverUrl")
    val coverUrl: String,
    @JsonProperty("thematiqueId")
    val thematiqueId: String,
    @JsonProperty("thematiqueLabel")
    val thematiqueLabel: String,
    @JsonProperty("thematiquePicto")
    val thematiquePicto: String,
    @JsonProperty("step")
    val step: Int,
)
