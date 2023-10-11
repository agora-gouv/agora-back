package fr.social.gouv.agora.infrastructure.supportQag.repository

import fr.social.gouv.agora.domain.SupportQag
import fr.social.gouv.agora.domain.SupportQagInfo
import fr.social.gouv.agora.infrastructure.utils.UuidUtils.toUuidOrNull
import fr.social.gouv.agora.usecase.supportQag.repository.GetSupportQagRepository
import org.springframework.stereotype.Component

@Component
class GetSupportQagRepositoryImpl(
    private val databaseRepository: SupportQagDatabaseRepository,
    private val mapper: SupportQagMapper,
) : GetSupportQagRepository {

    override fun getAllSupportQag(): List<SupportQagInfo> {
        return TODO("Remove")
    }

    override fun getSupportQag(qagId: String, userId: String): SupportQag? {
        return TODO("Remove")
    }

    override fun getQagSupportCounts(qagIds: List<String>): Map<String, Int> {
        // TODO tests
        return databaseRepository
            .geQagSupportCounts(qagIds.mapNotNull { it.toUuidOrNull() })
            .map { (qagUUID, supportCount) -> qagUUID.toString() to supportCount }
            .toMap()
    }

    override fun getUserSupportedQags(userId: String): List<String> {
        // TODO tests
        return userId.toUuidOrNull()?.let { userUUID ->
            databaseRepository.getUserSupportedQags(userUUID)
                .map { qagUUID -> qagUUID.toString() }
        } ?: emptyList()
    }

}