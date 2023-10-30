package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class GetSupportQagRepositoryImpl(
    private val databaseRepository: SupportQagDatabaseRepository,
) : GetSupportQagRepository {

    override fun getUserSupportedQags(userId: String): List<String> {
        return userId.toUuidOrNull()?.let { userUUID ->
            databaseRepository.getUserSupportedQags(userUUID)
                .map { qagUUID -> qagUUID.toString() }
        } ?: emptyList()
    }

    override fun isQagSupported(userId: String, qagId: String): Boolean {
        return userId.toUuidOrNull()?.let { userUUID ->
            qagId.toUuidOrNull()?.let { qagUUID ->
                databaseRepository.getSupportQag(userId = userUUID, qagId = qagUUID) != null
            }
        } ?: false
    }

}