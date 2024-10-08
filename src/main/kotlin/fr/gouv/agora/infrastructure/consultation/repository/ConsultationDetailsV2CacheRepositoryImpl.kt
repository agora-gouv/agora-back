package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.ConsultationDetailsV2
import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.FeedbackQuestion
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.Footer
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.Goal
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.InfoHeader
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.ResponsesInfo
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.Section
import fr.gouv.agora.domain.FeedbackConsultationUpdateStats
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateCacheResult
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateUserFeedbackCacheResult
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class ConsultationDetailsV2CacheRepositoryImpl(
    private val cacheManager: CacheManager,
    @Qualifier("shortTermCacheManager")
    private val shortTermCacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationDetailsV2CacheRepository {

    companion object {
        private const val CONSULTATION_DETAILS_LATEST_CACHE_NAME = "latestConsultationDetailsV2"
        private const val CONSULTATION_DETAILS_CACHE_NAME = "consultationDetailsV2"

        private const val HAS_ANSWERED_CACHE_NAME = "hasAnsweredConsultationDetailsV2"
        private const val PARTICIPANT_COUNT_CACHE_NAME = "participantCountConsultationDetailsV2"
        private const val HAS_GIVEN_FEEDBACK_CACHE_NAME = "hasGivenFeedbackConsultationUpdateV2"

        private const val UNANSWERED_USERS_CONSULTATION_DETAILS_PREFIX = "unanswered/"

        private const val SECTION_TYPE_TITLE = "title"
        private const val SECTION_TYPE_RICH_TEXT = "richText"
        private const val SECTION_TYPE_IMAGE = "image"
        private const val SECTION_TYPE_VIDEO = "video"
        private const val SECTION_TYPE_FOCUS_NUMBER = "focusNumber"
        private const val SECTION_TYPE_QUOTE = "quote"
        private const val SECTION_TYPE_ACCORDION = "accordion"
    }

    override fun getLastConsultationDetails(consultationId: String): ConsultationUpdateCacheResult {
        return getConsultationDetailsCache(
            cacheName = CONSULTATION_DETAILS_LATEST_CACHE_NAME,
            cacheKey = consultationId,
        )
    }

    override fun initLastConsultationDetails(consultationId: String, details: ConsultationDetailsV2?) {
        initConsultationDetailsCache(
            cacheName = CONSULTATION_DETAILS_LATEST_CACHE_NAME,
            cacheKey = consultationId,
            details = details,
        )
    }

    override fun clearLastConsultationDetails() {
        cacheManager.getCache(CONSULTATION_DETAILS_LATEST_CACHE_NAME)?.clear()
    }

    override fun getUnansweredUsersConsultationDetails(consultationId: String): ConsultationUpdateCacheResult {
        return getConsultationDetailsCache(
            cacheName = CONSULTATION_DETAILS_CACHE_NAME,
            cacheKey = "$UNANSWERED_USERS_CONSULTATION_DETAILS_PREFIX/$consultationId",
        )
    }

    override fun initUnansweredUsersConsultationDetails(consultationId: String, details: ConsultationDetailsV2?) {
        initConsultationDetailsCache(
            cacheName = CONSULTATION_DETAILS_CACHE_NAME,
            cacheKey = "$UNANSWERED_USERS_CONSULTATION_DETAILS_PREFIX/$consultationId",
            details = details,
        )
    }

    override fun getConsultationDetails(
        consultationId: String,
        consultationUpdateId: String,
    ): ConsultationUpdateCacheResult {
        return getConsultationDetailsCache(
            cacheName = CONSULTATION_DETAILS_CACHE_NAME,
            cacheKey = "$consultationId/$consultationUpdateId",
        )
    }

    override fun initConsultationDetails(
        consultationId: String,
        consultationUpdateId: String,
        details: ConsultationDetailsV2?,
    ) {
        initConsultationDetailsCache(
            cacheName = CONSULTATION_DETAILS_CACHE_NAME,
            cacheKey = "$consultationId/$consultationUpdateId",
            details = details,
        )
    }

    override fun getParticipantCount(consultationId: String): Int? {
        return try {
            return shortTermCacheManager
                .getCache(PARTICIPANT_COUNT_CACHE_NAME)
                ?.get(consultationId, String::class.java)
                ?.toIntOrNull()
        } catch (e: Exception) {
            null
        }
    }

    override fun initParticipantCount(consultationId: String, participantCount: Int) {
        shortTermCacheManager
            .getCache(PARTICIPANT_COUNT_CACHE_NAME)
            ?.put(consultationId, participantCount.toString())
    }

    override fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean? {
        return try {
            cacheManager.getCache(HAS_ANSWERED_CACHE_NAME)?.get("$consultationId/$userId")?.get()?.let { it == true }
        } catch (e: Exception) {
            null
        }
    }

    override fun initHasAnsweredConsultation(consultationId: String, userId: String, hasAnswered: Boolean) {
        cacheManager.getCache(HAS_ANSWERED_CACHE_NAME)?.put("$consultationId/$userId", hasAnswered)
    }

    override fun evictHasAnsweredConsultation(consultationId: String, userId: String) {
        cacheManager.getCache(HAS_ANSWERED_CACHE_NAME)?.evict("$consultationId/$userId")
    }

    override fun getUserFeedback(
        consultationUpdateId: String,
        userId: String,
    ): ConsultationUpdateUserFeedbackCacheResult {
        return try {
            val cachedContent = cacheManager.getCache(HAS_GIVEN_FEEDBACK_CACHE_NAME)
                ?.get("$consultationUpdateId/$userId", String::class.java)
            when (cachedContent) {
                null -> ConsultationUpdateUserFeedbackCacheResult.CacheNotInitialized
                "" -> ConsultationUpdateUserFeedbackCacheResult.CacheNotInitialized
                else -> ConsultationUpdateUserFeedbackCacheResult.CachedConsultationUpdateUserFeedback(
                    isUserFeedbackPositive = cachedContent.toBooleanStrictOrNull()
                )
            }
        } catch (e: Exception) {
            ConsultationUpdateUserFeedbackCacheResult.CacheNotInitialized
        }
    }

    override fun initUserFeedback(consultationUpdateId: String, userId: String, isUserFeedbackPositive: Boolean?) {
        cacheManager.getCache(HAS_GIVEN_FEEDBACK_CACHE_NAME)
            ?.put("$consultationUpdateId/$userId", isUserFeedbackPositive?.toString() ?: "null")
    }

    private fun getConsultationDetailsCache(
        cacheName: String,
        cacheKey: String,
    ): ConsultationUpdateCacheResult {
        return try {
            when (val cachedValue = cacheManager.getCache(cacheName)?.get(cacheKey, String::class.java)) {
                null -> ConsultationUpdateCacheResult.CacheNotInitialized
                "" -> ConsultationUpdateCacheResult.CacheNotInitialized
                else -> ConsultationUpdateCacheResult.CachedConsultationsDetails(
                    details = fromCacheable(
                        objectMapper.readValue(
                            cachedValue,
                            CacheableConsultationDetails::class.java,
                        )
                    )
                )
            }
        } catch (e: Exception) {
            ConsultationUpdateCacheResult.CacheNotInitialized
        }
    }

    private fun initConsultationDetailsCache(cacheName: String, cacheKey: String, details: ConsultationDetailsV2?) {
        cacheManager.getCache(cacheName)?.put(
            cacheKey,
            objectMapper.writeValueAsString(details?.let(::toCacheable) ?: ""),
        )
    }

    private fun toCacheable(details: ConsultationDetailsV2) = CacheableConsultationDetails(
        consultation = details.consultation,
        thematique = details.consultation.thematique,
        update = CacheableConsultationUpdateInfo(
            id = details.update.id,
            slug = details.update.slug,
            updateDate = details.update.updateDate,
            shareTextTemplate = details.update.shareTextTemplate,
            hasQuestionsInfo = details.update.hasQuestionsInfo,
            hasParticipationInfo = details.update.hasParticipationInfo,
            responsesInfo = details.update.responsesInfo,
            infoHeader = details.update.infoHeader,
            sectionsHeader = details.update.sectionsHeader.map(::toCacheableSection),
            body = details.update.body.map(::toCacheableSection),
            bodyPreview = details.update.bodyPreview.map(::toCacheableSection),
            downloadAnalysisUrl = details.update.downloadAnalysisUrl,
            feedbackQuestion = details.update.feedbackQuestion,
            footer = details.update.footer,
            goals = details.update.goals,
        ),
        feedbackStats = details.feedbackStats,
    )

    private fun fromCacheable(cacheable: CacheableConsultationDetails): ConsultationDetailsV2 {
        return ConsultationDetailsV2(
            consultation = cacheable.consultation,
            update = ConsultationUpdateInfoV2(
                id = cacheable.update.id,
                slug = cacheable.update.slug,
                updateDate = cacheable.update.updateDate,
                shareTextTemplate = cacheable.update.shareTextTemplate,
                hasQuestionsInfo = cacheable.update.hasQuestionsInfo,
                hasParticipationInfo = cacheable.update.hasParticipationInfo,
                responsesInfo = cacheable.update.responsesInfo,
                infoHeader = cacheable.update.infoHeader,
                sectionsHeader = cacheable.update.sectionsHeader.mapNotNull(::fromCacheableSection),
                body = cacheable.update.body.mapNotNull(::fromCacheableSection),
                bodyPreview = cacheable.update.bodyPreview.mapNotNull(::fromCacheableSection),
                downloadAnalysisUrl = cacheable.update.downloadAnalysisUrl,
                feedbackQuestion = cacheable.update.feedbackQuestion,
                goals = cacheable.update.goals,
                footer = cacheable.update.footer,
                isPublished = true,
            ),
            feedbackStats = cacheable.feedbackStats,
        )
    }

    private fun toCacheableSection(section: Section): CacheableSection {
        return when (section) {
            is Section.Title -> CacheableSection(type = SECTION_TYPE_TITLE, title = section.title)
            is Section.RichText -> CacheableSection(type = SECTION_TYPE_RICH_TEXT, description = section.description)
            is Section.Image -> CacheableSection(
                type = SECTION_TYPE_IMAGE,
                url = section.url,
                title = section.contentDescription,
            )

            is Section.Video -> CacheableSection(
                type = SECTION_TYPE_VIDEO,
                url = section.url,
                videoWidth = section.width,
                videoHeight = section.height,
                authorInfo = section.authorInfo,
                videoTranscription = section.transcription,
            )

            is Section.FocusNumber -> CacheableSection(
                type = SECTION_TYPE_FOCUS_NUMBER,
                title = section.title,
                description = section.description,
            )

            is Section.Quote -> CacheableSection(type = SECTION_TYPE_QUOTE, description = section.description)

            is Section.Accordion -> CacheableSection(
                type = SECTION_TYPE_ACCORDION,
                title = section.title,
                subSections = section.sections.map(::toCacheableSection),
            )
        }
    }

    private fun fromCacheableSection(section: CacheableSection): Section? {
        return when (section.type) {
            SECTION_TYPE_TITLE -> section.title?.let(Section::Title)
            SECTION_TYPE_RICH_TEXT -> section.description?.let(Section::RichText)
            SECTION_TYPE_IMAGE -> section.url?.let {
                Section.Image(
                    url = section.url,
                    contentDescription = section.title,
                )
            }

            SECTION_TYPE_VIDEO -> if (
                section.url != null &&
                section.videoWidth != null &&
                section.videoHeight != null &&
                section.videoTranscription != null
            ) Section.Video(
                url = section.url,
                width = section.videoWidth,
                height = section.videoHeight,
                authorInfo = section.authorInfo,
                transcription = section.videoTranscription,
            ) else null

            SECTION_TYPE_FOCUS_NUMBER -> if (section.title != null && section.description != null) Section.FocusNumber(
                title = section.title,
                description = section.description,
            ) else null

            SECTION_TYPE_QUOTE -> section.description?.let(Section::Quote)

            SECTION_TYPE_ACCORDION -> if (section.title != null && section.subSections != null) {
                Section.Accordion(
                    title = section.title,
                    sections = section.subSections.mapNotNull(::fromCacheableSection),
                )
            } else null

            else -> null
        }
    }

}

private data class CacheableConsultationDetails(
    val consultation: ConsultationInfo,
    val thematique: Thematique,
    val update: CacheableConsultationUpdateInfo,
    val feedbackStats: FeedbackConsultationUpdateStats?,
)

private data class CacheableConsultationUpdateInfo(
    val id: String,
    val slug: String,
    val updateDate: LocalDateTime,
    val shareTextTemplate: String,
    val hasQuestionsInfo: Boolean,
    val hasParticipationInfo: Boolean,
    val responsesInfo: ResponsesInfo?,
    val infoHeader: InfoHeader?,
    val sectionsHeader: List<CacheableSection>,
    val body: List<CacheableSection>,
    val bodyPreview: List<CacheableSection>,
    val downloadAnalysisUrl: String?,
    val feedbackQuestion: FeedbackQuestion?,
    val footer: Footer?,
    val goals: List<Goal>?,
)

private data class CacheableSection(
    val type: String,
    val title: String? = null,
    val description: String? = null,
    val url: String? = null,
    val videoWidth: Int? = null,
    val videoHeight: Int? = null,
    val authorInfo: Section.Video.AuthorInfo? = null,
    val videoTranscription: String? = null,
    val subSections: List<CacheableSection>? = null,
)
