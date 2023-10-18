package fr.social.gouv.agora.usecase.feedbackQag.repository

import fr.social.gouv.agora.domain.FeedbackResults

interface FeedbackResultsCacheRepository {
    fun getFeedbackResults(qagId: String): FeedbackResultsCacheResult
    fun initFeedbackResults(qagId: String, results: FeedbackResults)
}

sealed class FeedbackResultsCacheResult {
    data class CachedFeedbackResults(val feedbackResults: FeedbackResults) : FeedbackResultsCacheResult()
    object FeedbackResultsCacheNotInitialized : FeedbackResultsCacheResult()
}