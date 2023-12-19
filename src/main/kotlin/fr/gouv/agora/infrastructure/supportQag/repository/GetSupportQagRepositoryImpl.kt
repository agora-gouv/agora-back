package fr.gouv.agora.infrastructure.supportQag.repository

import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
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

    override fun getSupportedQagCount(userId: String, thematiqueId: String?): Int {
        return userId.toUuidOrNull()?.let { userUUID ->
            thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
                databaseRepository.getSupportedQagCountByThematique(
                    userId = userUUID,
                    thematiqueId = thematiqueUUID
                )
            } ?: databaseRepository.getSupportedQagCount(userUUID)
        } ?: 0
    }
}