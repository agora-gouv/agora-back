package fr.social.gouv.agora.usecase.feedbackQag

import fr.social.gouv.agora.domain.FeedbackQag
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.social.gouv.agora.usecase.feedbackQag.repository.FeedbackQagResult
import org.springframework.stereotype.Service

@Service
class InsertFeedbackQagUseCase(private val repository: FeedbackQagRepository) {
    fun insertFeedbackQag(feedbackQag: FeedbackQag): FeedbackQagResult {
        return repository.insertFeedbackQag(feedbackQag)
    }
}