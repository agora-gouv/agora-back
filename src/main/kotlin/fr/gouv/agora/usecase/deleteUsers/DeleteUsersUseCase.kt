package fr.gouv.agora.usecase.deleteUsers

import fr.gouv.agora.usecase.feedbackQag.repository.FeedbackQagRepository
import fr.gouv.agora.usecase.login.repository.UserDataRepository
import fr.gouv.agora.usecase.login.repository.UserRepository
import fr.gouv.agora.usecase.notification.repository.NotificationRepository
import fr.gouv.agora.usecase.profile.repository.ProfileRepository
import fr.gouv.agora.usecase.qag.repository.QagInfoRepository
import fr.gouv.agora.usecase.consultationResponse.repository.GetConsultationResponseRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class DeleteUsersUseCase(
    private val notificationRepository: NotificationRepository,
    private val consultationResponseRepository: GetConsultationResponseRepository,
    private val supportQagRepository: SupportQagRepository,
    private val qagInfoRepository: QagInfoRepository,
    private val feedbackQagRepository: FeedbackQagRepository,
    private val profileRepository: ProfileRepository,
    private val userDataRepository: UserDataRepository,
    private val userRepository: UserRepository,
) {
    private val logger: Logger = LoggerFactory.getLogger(DeleteUsersUseCase::class.java)

    fun deleteUsers(userIdsToDelete: List<String>) {
        logger.info("💁❌ Delete ${userIdsToDelete.size} user(s) with ID:\n${userIdsToDelete.joinToString("\n")}")
        if (userIdsToDelete.isEmpty()) return
        notificationRepository.deleteUsersNotifications(userIdsToDelete)
        consultationResponseRepository.deleteUserConsultationResponses(userIdsToDelete)
        supportQagRepository.deleteUsersSupportQag(userIdsToDelete)
        qagInfoRepository.deleteUsersQag(userIdsToDelete)
        feedbackQagRepository.deleteUsersFeedbackQag(userIdsToDelete)
        profileRepository.deleteUsersProfile(userIdsToDelete)
        userDataRepository.deleteUsersData(userIdsToDelete)
        userRepository.deleteUsers(userIdsToDelete)
    }

}
