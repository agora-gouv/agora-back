package fr.gouv.agora.infrastructure.consultation.repository

import com.fasterxml.jackson.databind.ObjectMapper
import fr.gouv.agora.domain.ConsultationDetailsV2
import fr.gouv.agora.domain.ConsultationUpdateHistory
import fr.gouv.agora.domain.ConsultationUpdateInfoV2
import fr.gouv.agora.domain.ConsultationUpdateInfoV2.*
import fr.gouv.agora.domain.Thematique
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationInfo
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateCacheResult
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.cache.CacheManager
import org.springframework.stereotype.Component
import java.util.*

@Component
@Suppress("unused")
class ConsultationDetailsV2CacheRepositoryImpl(
    private val cacheManager: CacheManager,
    @Qualifier("shortTermCacheManager")
    private val shortTermCacheManager: CacheManager,
    private val objectMapper: ObjectMapper,
) : ConsultationDetailsV2CacheRepository {

    companion object {
        private const val CONSULTATION_DETAILS_CACHE_NAME = "consultationDetailsV2"
        private const val CONSULTATION_INFO_CACHE_NAME = "consultationDetailsInfoV2"

        private const val UNANSWERED_USERS_CONSULTATION_DETAILS_PREFIX = "unanswered/"
        private const val LATEST_CONSULTATION_DETAILS_PREFIX = "latest/"

        private const val PARTICIPANT_COUNT_PREFIX = "participantCount/"
        private const val HAS_ANSWERED_PREFIX = "hasAnswered/"

        private const val SECTION_TYPE_TITLE = "title"
        private const val SECTION_TYPE_RICH_TEXT = "richText"
        private const val SECTION_TYPE_IMAGE = "image"
        private const val SECTION_TYPE_VIDEO = "video"
        private const val SECTION_TYPE_FOCUS_NUMBER = "focusNumber"
        private const val SECTION_TYPE_QUOTE = "quote"
    }

    override fun getUnansweredUsersConsultationDetails(consultationId: String): ConsultationUpdateCacheResult {
        return try {
            when (val cachedValue =
                getCache()?.get("$UNANSWERED_USERS_CONSULTATION_DETAILS_PREFIX/$consultationId", String::class.java)) {
                null -> ConsultationUpdateCacheResult.CacheNotInitialized
                "" -> ConsultationUpdateCacheResult.ConsultationUpdateNotFound
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

    override fun initUnansweredUsersConsultationDetails(
        consultationId: String,
        consultationDetails: ConsultationDetailsV2?,
    ) {
        getCache()?.put(
            "$UNANSWERED_USERS_CONSULTATION_DETAILS_PREFIX/$consultationId",
            objectMapper.writeValueAsString(consultationDetails?.let(::toCacheable) ?: ""),
        )
    }

    override fun getLastConsultationDetails(consultationId: String): ConsultationUpdateCacheResult {
        return try {
            when (val cachedValue =
                getCache()?.get("$LATEST_CONSULTATION_DETAILS_PREFIX/$consultationId", String::class.java)) {
                null -> ConsultationUpdateCacheResult.CacheNotInitialized
                "" -> ConsultationUpdateCacheResult.ConsultationUpdateNotFound
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

    override fun initLastConsultationDetails(consultationId: String, consultationDetails: ConsultationDetailsV2?) {
        getCache()?.put(
            "$LATEST_CONSULTATION_DETAILS_PREFIX/$consultationId",
            objectMapper.writeValueAsString(consultationDetails?.let(::toCacheable) ?: ""),
        )
    }

    override fun getParticipantCount(consultationId: String): Int? {
        return try {
            return getShortTermCache()
                ?.get("$PARTICIPANT_COUNT_PREFIX/$consultationId", String::class.java)
                ?.toIntOrNull()
        } catch (e: Exception) {
            null
        }
    }

    override fun initParticipantCount(consultationId: String, participantCount: Int) {
        getShortTermCache()?.put("$PARTICIPANT_COUNT_PREFIX/$consultationId", participantCount.toString())
    }

    override fun hasAnsweredConsultation(consultationId: String, userId: String): Boolean? {
        return try {
            getCache()?.get("$HAS_ANSWERED_PREFIX/$consultationId/$userId")?.get().let { it == true }
        } catch (e: Exception) {
            null
        }
    }

    override fun initHasAnsweredConsultation(consultationId: String, userId: String, hasAnswered: Boolean) {
        getCache()?.put("$HAS_ANSWERED_PREFIX/$consultationId/$userId", hasAnswered)
    }

    override fun evictHasAnsweredConsultation(consultationId: String, userId: String) {
        getCache()?.evict("$HAS_ANSWERED_PREFIX/$consultationId/$userId")
    }

    private fun getCache() = cacheManager.getCache(CONSULTATION_DETAILS_CACHE_NAME)
    private fun getShortTermCache() = shortTermCacheManager.getCache(CONSULTATION_INFO_CACHE_NAME)

    private fun toCacheable(details: ConsultationDetailsV2) = CacheableConsultationDetails(
        consultation = details.consultation,
        thematique = details.thematique,
        update = CacheableConsultationUpdateInfo(
            id = details.update.id,
            updateDate = details.update.updateDate,
            shareTextTemplate = details.update.shareTextTemplate,
            hasQuestionsInfo = details.update.hasQuestionsInfo,
            hasParticipationInfo = details.update.hasParticipationInfo,
            responsesInfo = details.update.responsesInfo,
            infoHeader = details.update.infoHeader,
            body = details.update.body.map(::toCacheableSection),
            bodyPreview = details.update.bodyPreview.map(::toCacheableSection),
            downloadAnalysisUrl = details.update.downloadAnalysisUrl,
            footer = details.update.footer,
        ),
        history = details.history,
    )

    private fun fromCacheable(cacheable: CacheableConsultationDetails): ConsultationDetailsV2 {
        return ConsultationDetailsV2(
            consultation = cacheable.consultation,
            thematique = cacheable.thematique,
            update = ConsultationUpdateInfoV2(
                id = cacheable.update.id,
                updateDate = cacheable.update.updateDate,
                shareTextTemplate = cacheable.update.shareTextTemplate,
                hasQuestionsInfo = cacheable.update.hasQuestionsInfo,
                hasParticipationInfo = cacheable.update.hasParticipationInfo,
                responsesInfo = cacheable.update.responsesInfo,
                infoHeader = cacheable.update.infoHeader,
                body = cacheable.update.body.mapNotNull(::fromCacheableSection),
                bodyPreview = cacheable.update.bodyPreview.mapNotNull(::fromCacheableSection),
                downloadAnalysisUrl = cacheable.update.downloadAnalysisUrl,
                footer = cacheable.update.footer,
            ),
            history = cacheable.history,
        )
    }

    private fun toCacheableSection(section: Section): CacheableSection {
        return when (section) {
            is Section.Title -> CacheableSection(type = SECTION_TYPE_TITLE, title = section.title)
            is Section.RichText -> CacheableSection(type = SECTION_TYPE_RICH_TEXT, description = section.description)
            is Section.Image -> CacheableSection(
                type = SECTION_TYPE_IMAGE,
                url = section.url,
                description = section.contentDescription,
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
        }
    }

    private fun fromCacheableSection(section: CacheableSection): Section? {
        return when (section.type) {
            SECTION_TYPE_TITLE -> section.title?.let(Section::Title)
            SECTION_TYPE_RICH_TEXT -> section.description?.let(Section::RichText)
            SECTION_TYPE_IMAGE -> section.url?.let {
                Section.Image(
                    url = section.url,
                    contentDescription = section.description,
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

            else -> null
        }
    }

}

private data class CacheableConsultationDetails(
    val consultation: ConsultationInfo,
    val thematique: Thematique,
    val update: CacheableConsultationUpdateInfo,
    val history: List<ConsultationUpdateHistory>?,
)

private data class CacheableConsultationUpdateInfo(
    val id: String,
    val updateDate: Date,
    val shareTextTemplate: String,
    val hasQuestionsInfo: Boolean,
    val hasParticipationInfo: Boolean,
    val responsesInfo: ResponsesInfo?,
    val infoHeader: InfoHeader?,
    val body: List<CacheableSection>,
    val bodyPreview: List<CacheableSection>,
    val downloadAnalysisUrl: String?,
    val footer: Footer?,
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
)