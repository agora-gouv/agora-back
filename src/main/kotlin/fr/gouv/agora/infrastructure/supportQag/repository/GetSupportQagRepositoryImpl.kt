package fr.gouv.agora.infrastructure.supportQag.repository

import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class GetSupportQagRepositoryImpl(
    private val databaseRepository: SupportQagDatabaseRepository,
) : GetSupportQagRepository {
    override fun getUserSupportedQags(userId: String, thematiqueId: String?): List<String> {
        val userUUID = userId.toUuidOrNull() ?: return emptyList()
        val qagUUIdList = thematiqueId?.toUuidOrNull()?.let { thematiqueUUID ->
            databaseRepository.getUserSupportedQagsByThematique(userUUID, thematiqueUUID)
        } ?: databaseRepository.getUserSupportedQags(userUUID)
        return qagUUIdList.map { qagUUID -> qagUUID.toString() }
    }

    override fun isQagSupported(userId: String, qagId: String): Boolean {
        return userId.toUuidOrNull()?.let { userUUID ->
            qagId.toUuidOrNull()?.let { qagUUID ->
                databaseRepository.getSupportQag(userId = userUUID, qagId = qagUUID) != null
            }
        } ?: false
    }
}