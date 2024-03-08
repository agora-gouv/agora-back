package fr.gouv.agora.usecase.feedbackConsultationUpdate

import fr.gouv.agora.domain.AgoraFeature
import fr.gouv.agora.domain.FeedbackConsultationUpdateInserting
import fr.gouv.agora.domain.FeedbackConsultationUpdateResults
import fr.gouv.agora.domain.FeedbackConsultationUpdateStats
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
        val newFeedbackStats =
            buildFeedbackStats(consultationUpdateId = feedbackInserting.consultationUpdateId)?.also { feedbackStats ->
                updateCache(
                    feedbackInserting = feedbackInserting,
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
            userId = userId
        )
    }

    private fun updateCache(
        feedbackInserting: FeedbackConsultationUpdateInserting,
        feedbackStats: FeedbackConsultationUpdateStats,
    ) {
        val consultationId = feedbackInserting.consultationId
        val consultationUpdateId = feedbackInserting.consultationUpdateId

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