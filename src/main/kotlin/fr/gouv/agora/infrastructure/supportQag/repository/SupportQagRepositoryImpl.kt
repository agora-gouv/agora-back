package fr.gouv.agora.infrastructure.supportQag.repository

import fr.gouv.agora.domain.SupportQagDeleting
import fr.gouv.agora.domain.SupportQagInserting
import fr.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.gouv.agora.usecase.supportQag.repository.SupportQagRepository
import fr.gouv.agora.usecase.supportQag.repository.SupportQagResult
import org.springframework.stereotype.Component

@Component
@Suppress("unused")
class SupportQagRepositoryImpl(
    private val databaseRepository: SupportQagDatabaseRepository,
    private val mapper: SupportQagMapper,
) : SupportQagRepository {

    override fun insertSupportQag(supportQagInserting: SupportQagInserting): SupportQagResult {
        return mapper.toDto(supportQagInserting)?.let { supportQagDTO ->
            databaseRepository.save(supportQagDTO)
            SupportQagResult.SUCCESS
        } ?: SupportQagResult.FAILURE
    }

    override fun deleteSupportQag(supportQagDeleting: SupportQagDeleting): SupportQagResult {
        return supportQagDeleting.qagId.toUuidOrNull()?.let { qagUUID ->
            supportQagDeleting.userId.toUuidOrNull()?.let { userUUID ->
                val resultDelete = databaseRepository.deleteSupportQag(userId = userUUID, qagId = qagUUID)
                if (resultDelete <= 0)
                    SupportQagResult.FAILURE
                else {
                    SupportQagResult.SUCCESS
                }
            }
        } ?: SupportQagResult.FAILURE
    }

    override fun deleteSupportListByQagId(qagId: String): SupportQagResult {
        return qagId.toUuidOrNull()?.let { qagUUID ->
            val resultDelete = databaseRepository.deleteSupportListByQagId(qagId = qagUUID)
            if (resultDelete <= 0)
                SupportQagResult.FAILURE
            else {
                SupportQagResult.SUCCESS
            }
        } ?: SupportQagResult.FAILURE
    }

    override fun deleteUsersSupportQag(userIDs: List<String>) {
        val userUUIDs = userIDs.mapNotNull { it.toUuidOrNull() }
        databaseRepository.deleteUserSupportsQags(userUUIDs)
        databaseRepository.anonymizeSupportsOnSelectedQags(userUUIDs)
        databaseRepository.deleteSupportsOnDeletedUsersQags(userUUIDs)
    }

}