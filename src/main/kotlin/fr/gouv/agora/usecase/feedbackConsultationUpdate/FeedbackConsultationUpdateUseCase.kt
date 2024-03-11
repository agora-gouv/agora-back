package fr.gouv.agora.usecase.feedbackConsultationUpdate

import fr.gouv.agora.domain.*
import fr.gouv.agora.usecase.consultation.repository.ConsultationDetailsV2CacheRepository
import fr.gouv.agora.usecase.consultation.repository.ConsultationUpdateCacheResult
import fr.gouv.agora.usecase.consultationUpdate.repository.ConsultationUpdateV2Repository
import fr.gouv.agora.usecase.featureFlags.repository.FeatureFlagsRepository
import fr.gouv.agora.usecase.feedbackConsultationUpdate.repository.FeedbackConsultationUpdateRepository
import org.springframework.stereotype.Component

@Component
class FeedbackConsultationUpdateUseCase(
    private val featureFlagsRepository: FeatureFlagsRepository,
    private val feedbackRepository: FeedbackConsultationUpdateRepository,
    private val consultationUpdateRepository: ConsultationUpdateV2Repository,
    private val cacheRepository: ConsultationDetailsV2CacheRepository,
) {

    fun insertFeedback(feedbackInserting: FeedbackConsultationUpdateInserting): InsertFeedbackConsultationUpdateResults {
        if (!canAcceptFeedbacks(consultationUpdateId = feedbackInserting.consultationUpdateId)) return InsertFeedbackConsultationUpdateResults.Failure
        if (getUserFeedback(
                consultationUpdateId = feedbackInserting.consultationUpdateId,
                userId = feedbackInserting.userId,
            ) != null
        ) return InsertFeedbackConsultationUpdateResults.Failure

        feedbackRepository.insertFeedback(feedbackInserting)
        cacheRepository.initUserFeedback(
            consultationUpdateId = feedbackInserting.consultationUpdateId,
            userId = feedbackInserting.userId,
            isUserFeedbackPositive = feedbackInserting.isPositive,
        )
        val newFeedbackStats =
            buildFeedbackStats(consultationUpdateId = feedbackInserting.consultationUpdateId)?.also { feedbackStats ->
                updateFeedbackStatsCache(
                    consultationId = feedbackInserting.consultationId,
                    consultationUpdateId = feedbackInserting.consultationUpdateId,
                    feedbackStats = feedbackStats,
                )
            }

        return InsertFeedbackConsultationUpdateResults.Success(
            FeedbackConsultationUpdateResults(
                userResponse = feedbackInserting.isPositive,
                stats = newFeedbackStats,
            )
        )
    }

    fun deleteFeedback(feedbackDeleting: FeedbackConsultationUpdateDeleting): Boolean {
        if (!canAcceptFeedbacks(consultationUpdateId = feedbackDeleting.consultationUpdateId)) return false
        if (getUserFeedback(
                consultationUpdateId = feedbackDeleting.consultationUpdateId,
                userId = feedbackDeleting.userId,
            ) == null
        ) return false

        feedbackRepository.deleteFeedback(feedbackDeleting)
        cacheRepository.initUserFeedback(
            consultationUpdateId = feedbackDeleting.consultationUpdateId,
            userId = feedbackDeleting.userId,
            isUserFeedbackPositive = null,
        )
        buildFeedbackStats(consultationUpdateId = feedbackDeleting.consultationUpdateId)?.also { feedbackStats ->
            updateFeedbackStatsCache(
                consultationId = feedbackDeleting.consultationId,
                consultationUpdateId = feedbackDeleting.consultationUpdateId,
                feedbackStats = feedbackStats,
            )
        }

        return true
    }

    private fun buildFeedbackStats(consultationUpdateId: String): FeedbackConsultationUpdateStats? {
        if (!featureFlagsRepository.isFeatureEnabled(AgoraFeature.FeedbackConsultationUpdate)) return null
        return feedbackRepository.getFeedbackStats(consultationUpdateId)
    }

    private fun canAcceptFeedbacks(consultationUpdateId: String): Boolean {
        return consultationUpdateRepository.getConsultationUpdate(consultationUpdateId)?.let { update ->
            update.feedbackQuestion != null
        } ?: false
    }

    private fun getUserFeedback(consultationUpdateId: String, userId: String): Boolean? {
        return feedbackRepository.getUserFeedback(
            consultationUpdateId = consultationUpdateId,
            userId = userId,
        )
    }

    private fun updateFeedbackStatsCache(
        consultationId: String,
        consultationUpdateId: String,
        feedbackStats: FeedbackConsultationUpdateStats,
    ) {
        val cachedLatest = cacheRepository.getLastConsultationDetails(consultationId = consultationId)
        if (cachedLatest is ConsultationUpdateCacheResult.CachedConsultationsDetails && cachedLatest.details.update.id == consultationUpdateId) {
            cacheRepository.initLastConsultationDetails(
                consultationId = consultationId,
                details = cachedLatest.details.copy(feedbackStats = feedbackStats),
            )
        }

        val cachedUpdate = cacheRepository.getConsultationDetails(
            consultationId = consultationId,
            consultationUpdateId = consultationUpdateId,
        )
        if (cachedUpdate is ConsultationUpdateCacheResult.CachedConsultationsDetails && cachedUpdate.details.update.id == consultationUpdateId) {
            cacheRepository.initConsultationDetails(
                consultationId = consultationId,
                consultationUpdateId = consultationUpdateId,
                details = cachedUpdate.details.copy(feedbackStats = feedbackStats),
            )
        }

    }

}

sealed class InsertFeedbackConsultationUpdateResults {
    data class Success(val feedbackResults: FeedbackConsultationUpdateResults) :
        InsertFeedbackConsultationUpdateResults()

    object Failure : InsertFeedbackConsultationUpdateResults()
}